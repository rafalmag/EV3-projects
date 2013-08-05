package lejos.internal.io;
import java.io.IOError;
import java.nio.ByteBuffer;
import com.sun.jna.Pointer;

public class NativeDevice extends NativeFile
{
    public NativeDevice(String dname)
    {
        super();
        try {
            open(dname, O_RDWR, 0);
        } catch(Exception e)
        {
            throw new IOError(e);
        }
    }
    
    public Pointer mmap(long len)
    {
        return super.mmap(len, PROT_READ | PROT_WRITE, MAP_SHARED, 0);
    }
}
