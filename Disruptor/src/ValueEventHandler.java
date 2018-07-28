import com.lmax.disruptor.WorkHandler;

public class ValueEventHandler implements WorkHandler<ValueEvent>{
	
	public int count=0;

    public void onEvent(final ValueEvent event) throws Exception{
    	if(Main.isPrime(event.getValue()))count++;
    }
			
}
