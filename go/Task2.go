package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Item struct {
	price int
}

type Storage struct {
	itemsLeft int
	mutex     sync.Mutex
}

func (s *Storage) name() string {
	return "Storage"
}

type Producer interface {
	produce() Item
	name() string
}

type Buffer interface {
	put(item Item)
	get() Item
}

type Consumer interface {
	consume(item Item)
	name() string
}

type Truck struct {
	arr       []Item
	lastAdded chan Item
	mutex     sync.Mutex
}

func (t *Truck) name() string {
	return "Truck"
}

func (t *Truck) produce() Item {
	return <-t.lastAdded
}

type Worker struct {
	workerName string
	myItem     *Item
	workTime   int
}

func (w *Worker) name() string {
	return w.workerName
}

func (w *Worker) consume(item Item) {
	w.myItem = &item
}

func (w *Worker) transfer() {
	time.Sleep(time.Duration(w.workTime) * time.Millisecond)
}

func (w *Worker) produce() Item {
	i := *w.myItem
	w.myItem = nil
	return i
}

func (t *Truck) consume(item Item) {
	t.lastAdded <- item
	t.mutex.Lock()
	t.arr = append(t.arr, item)
	t.mutex.Unlock()
}

type Counter struct {
	counterName string
	count       int
	numItems    int
	mutex       sync.Mutex
}

func (c *Counter) name() string {
	return c.counterName
}

func (c *Counter) consume(item Item) {
	c.mutex.Lock()
	c.add(item.price)
	c.mutex.Unlock()
}

func (c *Counter) add(val int) {
	c.count += val
	c.numItems++
}

func (c *Counter) getPrice() int {
	return c.count
}
func (c *Counter) getNum() int {
	return c.numItems
}

func (s *Storage) produce() Item {
	s.mutex.Lock()
	if s.itemsLeft < 1 {
		return Item{-1}
	}
	s.itemsLeft--
	s.mutex.Unlock()
	return Item{rand.Intn(100)}
}
func (s *Storage) create(to BufferS) {
	for s.itemsLeft > 0 {
		item := s.produce()
		//fmt.Println(s.name(), "produces", item.price)
		to.buffer <- item
	}
	fmt.Println("Storage is empty!")
	to.done(0)
}

func (t *Truck) pack(from BufferS, to BufferS) {
	for {
		select {
		case item := <-from.buffer:
			t.arr = append(t.arr, item)
			//fmt.Println(t.name(), "packs", item.price)
			to.buffer <- item
		default:
			select {
			case done := <-from.isDone:
				to.done(done)
				break
			default:
				continue
			}
		}
	}
}

func (w *Worker) work(fromBuf BufferS, toBuf BufferS) {
	for {
		select {
		case item := <-fromBuf.buffer:
			w.transfer()
			//fmt.Println(w.name(), "transfers", item.price)
			toBuf.buffer <- item
		default:
			select {
			case done := <-fromBuf.isDone:
				toBuf.done(done)
				break
			default:
				continue
			}
		}
	}
}

func (c *Counter) doCount(from BufferS, group *sync.WaitGroup) {
	for {
		select {
		case item := <-from.buffer:
			fmt.Println(c.name(), "counts", item.price)
			c.consume(item)
		default:
			select {
			case <-from.isDone:
				group.Done()
				return
			default:
				continue
			}

		}
	}
}

type BufferS struct {
	buffer chan Item
	isDone chan int
	group  *sync.WaitGroup
}

func (b *BufferS) close() {
	close(b.buffer)
	close(b.isDone)
}
func (b *BufferS) done(v int) {
	b.isDone <- v
	b.group.Done()
}

func createChannel(group *sync.WaitGroup) BufferS {
	const BufferSize = 10
	return BufferS{make(chan Item, BufferSize), make(chan int), group}
}

func main() {
	//Storage -> Ivanov -> Petrov -> Truck -> Necheporchuk
	storage := Storage{20, sync.Mutex{}}

	group := sync.WaitGroup{}
	group.Add(5)

	ivanov := Worker{"Ivanov", nil, 10}
	storageChannel := createChannel(&group)

	petrov := Worker{"Petrov", nil, 15}
	ivanovChannel := createChannel(&group)

	truck := Truck{}
	petrovChannel := createChannel(&group)

	necheporchuk := Counter{"Necheporchuk", 0, 0, sync.Mutex{}}
	truckChannel := createChannel(&group)

	go storage.create(storageChannel)
	go ivanov.work(storageChannel, ivanovChannel)
	go petrov.work(ivanovChannel, petrovChannel)
	go truck.pack(petrovChannel, truckChannel)
	go necheporchuk.doCount(truckChannel, &group)

	group.Wait()
	storageChannel.close()
	ivanovChannel.close()
	petrovChannel.close()
	truckChannel.close()
	fmt.Println("Total items:", necheporchuk.getNum())
	fmt.Println("Total price:", necheporchuk.getPrice())
}
