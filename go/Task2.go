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

func (s *Storage) produce() Item {
	if s.itemsLeft < 1 {
		return Item{-1}
	}
	s.itemsLeft--
	return Item{rand.Intn(100)}
}

type ChanBuffer struct {
	ch chan Item
}

func (c ChanBuffer) put(item Item) {
	c.ch <- item
}

func (c ChanBuffer) get() Item {
	return <-c.ch
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
	c.add(item.price)
}

func (c *Counter) add(val int) {
	c.mutex.Lock()
	c.count += val
	c.numItems++
	c.mutex.Unlock()
}

func (c *Counter) getPrice() int {
	return c.count
}
func (c *Counter) getNum() int {
	return c.numItems
}

func (s *Storage) create(to chan Item) {
	for s.itemsLeft > 0 {
		item := s.produce()
		fmt.Println(s.name(), "produces", item.price)
		to <- item
	}
}

func (t *Truck) pack(from chan Item, to chan Item) {
	for {
		item := <-from
		t.arr = append(t.arr, item)
		fmt.Println(t.name(), "packs", item.price)
		to <- item
	}
}

func (w *Worker) work(fromBuf chan Item, toBuf chan Item) {
	for {
		item := <-fromBuf
		w.transfer()
		fmt.Println(w.name(), "transfers", item.price)
		toBuf <- item
	}
}

func (c *Counter) doCount(from chan Item) {
	for {
		item := <-from
		fmt.Println(c.name(), "counts", item.price)
		c.consume(item)
	}
}

func main() {
	//Storage -> Ivanov -> Petrov -> Truck -> Necheporchuk
	storage := Storage{20}

	const BUFSIZE = 10

	ivanov := Worker{"Ivanov", nil, 10}
	storageChannel := make(chan Item, BUFSIZE)

	petrov := Worker{"Petrov", nil, 15}
	ivanovChannel := make(chan Item, BUFSIZE)

	truck := Truck{}
	petrovChannel := make(chan Item, BUFSIZE)

	necheporchuk := Counter{"Necheporhuck", 0, 0, sync.Mutex{}}
	truckChannel := make(chan Item, BUFSIZE)

	group := sync.WaitGroup{}
	group.Add(5)

	go storage.create(storageChannel)
	go ivanov.work(storageChannel, ivanovChannel)
	go petrov.work(ivanovChannel, petrovChannel)
	go truck.pack(petrovChannel, truckChannel)
	go necheporchuk.doCount(truckChannel)

	group.Wait()
	close(storageChannel)
	close(ivanovChannel)
	close(petrovChannel)
	close(truckChannel)
	fmt.Println(necheporchuk.getNum())
	fmt.Println(necheporchuk.getPrice())
}
