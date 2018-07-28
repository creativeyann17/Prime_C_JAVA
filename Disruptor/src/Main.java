
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;


public class Main {
	
	public static final int THREADS = Runtime.getRuntime().availableProcessors();
	public static final int SIZE = 1000000;
	public static final int RING_SIZE = 1024;
	
	public static boolean isPrime(int value){
		for(int j=2;j<value;++j){
			if(value%j==0){
				return false;
			}
		}
		return true;
	}

	public static void main(String[]args){
		long start = System.currentTimeMillis();
		
		int count=0;
		/*for (int i = 0; i < SIZE; ++i) {
			if(isPrime(i))count++;
		}
		
		System.out.println("Iterative: "+count+" in "+(System.currentTimeMillis() - start));
		
		start=System.currentTimeMillis();*/
		
		ExecutorService exec = Executors.newFixedThreadPool(THREADS);
		// Preallocate RingBuffer with 1024 ValueEvents
		Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, RING_SIZE, exec);
		
		ValueEventHandler [] handlers=new ValueEventHandler[THREADS];
		for(int i=0;i<THREADS;++i){
			handlers[i]=new ValueEventHandler();
		}
		
		// Build dependency graph
		disruptor.handleEventsWithWorkerPool(handlers);
		
		RingBuffer<ValueEvent> ringBuffer = disruptor.start();

		for (long i = 0; i < SIZE; i++) {
			long seq = ringBuffer.next();
			ValueEvent valueEvent = ringBuffer.get(seq);
			valueEvent.setValue((int)i);
			ringBuffer.publish(seq);
		}
		
		disruptor.shutdown();
		exec.shutdown();
		
		count=0;
		for(int i=0;i<THREADS;++i){
			count+=handlers[i].count;
		}
		
		System.out.println("Disruptor: "+count+" in "+(System.currentTimeMillis() - start));

		
	}
}
