import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Main extends RecursiveAction {
	
	private static final long serialVersionUID = 1L;
	
	public static final int THREADS = Runtime.getRuntime().availableProcessors();
	public static final int SIZE = 1000000;
	public static final int THRESHOLD = 1024;
	
	public static int count=0;
	
	public synchronized static void inc(int c){
		Main.count+=c;
	}
	
	public static boolean isPrime(int value){
		for(int j=2;j<value;++j){
			if(value%j==0){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		
		/*long count=0;
		for (int i = 0; i < SIZE; ++i) {
			if(isPrime(i))count++;
		}
		
		System.out.println("Iterative: "+count+" in "+(System.currentTimeMillis() - start));
		
		start=System.currentTimeMillis();*/
		
		Main fb = new Main(0,SIZE);
		
		ForkJoinPool pool = new ForkJoinPool(THREADS);

		pool.invoke(fb);
		
		System.out.println("ForkJoin "+count+" in "+(System.currentTimeMillis() - start));
	}
	
	private int start;
	private int end;
	
	public Main(int start,int end){
		this.start=start;
		this.end=end;
	}
	
	protected void compute() {
		int length=(end-start);
		//int count=0;
		if(length<=THRESHOLD){
			int count=0;
			for(int i=start;i<end;++i){
				if(isPrime(i)){
					count++;
				}
			}
			inc(count);
		}else{
			int mid = start + ((end - start)/2);
			invokeAll(new Main(start, mid),new Main(mid, end));
			/*Main m1=new Main(start, mid);
			Main m2=new Main(mid, end);
			System.out.println("Fork: "+Thread.currentThread().getId());
			m1.fork();m2.fork();
			count=(m1.join()+m2.join());*/
		}
	}

}
