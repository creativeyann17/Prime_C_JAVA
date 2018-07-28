import com.lmax.disruptor.EventFactory;

public final class ValueEvent
{
    private int value;

    public int getValue()
    {
        return value;
    }

    public void setValue(final int value)
    {
        this.value = value;
    }

    public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>()
    {
        public ValueEvent newInstance()
        {
            return new ValueEvent();
        }
    };
}
