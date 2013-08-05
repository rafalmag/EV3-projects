package lejos.internal.io;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.nio.channels.FileChannel;

public class NativeFile
{
    static final int O_ACCMODE = 0003;
    static final int O_RDONLY = 00;
    static final int O_WRONLY = 01;
    static final int O_RDWR = 02;
    static final int O_CREAT = 0100;
    static final int O_EXCL = 0200;
    static final int O_NOCTTY = 0400;
    static final int O_TRUNC = 01000;
    static final int O_APPEND = 02000;
    static final int O_NONBLOCK = 04000;
    static final int O_NDELAY = O_NONBLOCK;
    static final int O_SYNC = 04010000;
    static final int O_FSYNC = O_SYNC;
    static final int O_ASYNC = 020000;
    static final int PROT_READ = 1;
    static final int PROT_WRITE = 2;
    static final int MAP_SHARED = 1;
    static final int MAP_PRIVATE = 2;
    static final int MAP_FILE = 0;
    

    static class Linux_C_lib_DirectMapping {
        native public int fcntl(int fd, int cmd, int arg);

        native public int ioctl(int fd, int cmd, byte[] arg);

        native public int ioctl(int fd, int cmd, Pointer p);
        
        native public int open(String path, int flags);

        native public int close(int fd);


        native public int write(int fd, Buffer buffer, int count);

        native public int read(int fd, Buffer buffer, int count);
        
        native public Pointer mmap(Pointer addr, NativeLong len, int prot, int flags, int fd,
                NativeLong off);

        static {
            try {
                Native.register("c");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    static Linux_C_lib_DirectMapping clib = new Linux_C_lib_DirectMapping();
    
    int fd;
    RandomAccessFile jfd;
    FileChannel fc;
    
    protected NativeFile()
    {
        
    }
    
    public NativeFile(String fname, int flags, int mode) throws FileNotFoundException
    {
        open(fname, flags, mode);
    }
    
    public void open(String fname, int flags, int mode) throws FileNotFoundException
    {
        //fd = CLibrary.INSTANCE.open(fname, flags, mode);
        fd = clib.open(fname, flags);
        if (fd < 0)
            throw new FileNotFoundException("File: " + fname + " errno " + Native.getLastError());
        jfd = new RandomAccessFile(fname, "rw");
        fc = jfd.getChannel();
    }
    
    public int read(byte[] buf, int len)
    {
        //return CLibrary.INSTANCE.read(fd, ByteBuffer.wrap(buf, 0, len), len);
        //return clib.read(fd, ByteBuffer.wrap(buf, 0, len), len);
        try
        {
            return fc.read(ByteBuffer.wrap(buf, 0, len));
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            return -1;
        }
    }
    
    public int write(byte[] buf, int offset, int len)
    {
        //return CLibrary.INSTANCE.write(fd, ByteBuffer.wrap(buf, offset, len), len);
        //return clib.write(fd, ByteBuffer.wrap(buf, offset, len), len);
        try
        {
            return fc.write(ByteBuffer.wrap(buf, offset, len));
        } catch (IOException e)
        {
            return -1;
        }
    }
    
    public int read(byte[] buf, int offset, int len)
    {
        //return CLibrary.INSTANCE.read(fd, ByteBuffer.wrap(buf, offset, len), len);
        //return clib.read(fd, ByteBuffer.wrap(buf, offset, len), len);
        try
        {
            return fc.read(ByteBuffer.wrap(buf, offset, len));
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            return -1;
        }
    }
    
    public int write(byte[] buf, int len)
    {
        //return CLibrary.INSTANCE.write(fd, ByteBuffer.wrap(buf, 0, len), len);
        //return clib.write(fd, ByteBuffer.wrap(buf, 0, len), len);
        try
        {
            return fc.write(ByteBuffer.wrap(buf, 0, len));
        } catch (IOException e)
        {
            return -1;
        }
    }
    
    public int ioctl(int req, byte[] buf)
    {
        //return CLibrary.INSTANCE.ioctl(fd, req, buf);
        return clib.ioctl(fd, req, buf);
    }
    
    public int ioctl(int req, Pointer buf)
    {
        //return CLibrary.INSTANCE.ioctl(fd, req, buf);
        return clib.ioctl(fd, req, buf);
    }
    
    public int close()
    {
        //return CLibrary.INSTANCE.close(fd);
        try
        {
            fc.close();
            jfd.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return clib.close(fd);
    }
    
    public Pointer mmap(long len, int prot, int flags, long off)
    {
        //Pointer p = CLibrary.INSTANCE.mmap(new Pointer(0), new NativeLong(len), prot, flags, fd, new NativeLong(off));
        Pointer p = clib.mmap(new Pointer(0), new NativeLong(len), prot, flags, fd, new NativeLong(off));
        if (p == null)
            return null;
        return p;
    }

}
