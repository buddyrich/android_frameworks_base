package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import android.util.Log;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import miui.app.screenelement.ResourceManager.ResourceLoader;
import miui.content.res.ThemeResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class LockScreenResourceLoader
  implements ResourceManager.ResourceLoader
{
  private static final String LOG_TAG = "LockScreenResourceLoader";

  // ERROR //
  public miui.app.screenelement.ResourceManager.BitmapInfo getBitmapInfo(String paramString)
  {
    // Byte code:
    //   0: invokestatic 26	miui/content/res/ThemeResources:getSystem	()Lmiui/content/res/ThemeResources;
    //   3: aload_1
    //   4: aconst_null
    //   5: invokevirtual 30	miui/content/res/ThemeResources:getAwesomeLockscreenFileStream	(Ljava/lang/String;[I)Ljava/io/InputStream;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnull +52 -> 62
    //   13: new 32	android/graphics/Rect
    //   16: dup
    //   17: invokespecial 33	android/graphics/Rect:<init>	()V
    //   20: astore 4
    //   22: new 35	miui/app/screenelement/ResourceManager$BitmapInfo
    //   25: dup
    //   26: aload_2
    //   27: aload 4
    //   29: aconst_null
    //   30: invokestatic 41	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   33: aload 4
    //   35: invokespecial 44	miui/app/screenelement/ResourceManager$BitmapInfo:<init>	(Landroid/graphics/Bitmap;Landroid/graphics/Rect;)V
    //   38: astore_3
    //   39: aload_2
    //   40: invokevirtual 49	java/io/InputStream:close	()V
    //   43: aload_3
    //   44: areturn
    //   45: astore 8
    //   47: ldc 51
    //   49: aload 8
    //   51: invokevirtual 55	java/lang/OutOfMemoryError:toString	()Ljava/lang/String;
    //   54: invokestatic 61	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   57: pop
    //   58: aload_2
    //   59: invokevirtual 49	java/io/InputStream:close	()V
    //   62: aconst_null
    //   63: astore_3
    //   64: goto -21 -> 43
    //   67: astore 6
    //   69: aload_2
    //   70: invokevirtual 49	java/io/InputStream:close	()V
    //   73: aload 6
    //   75: athrow
    //   76: astore 5
    //   78: goto -35 -> 43
    //   81: astore 10
    //   83: goto -21 -> 62
    //   86: astore 7
    //   88: goto -15 -> 73
    //
    // Exception table:
    //   from	to	target	type
    //   13	39	45	java/lang/OutOfMemoryError
    //   13	39	67	finally
    //   47	58	67	finally
    //   39	43	76	java/io/IOException
    //   58	62	81	java/io/IOException
    //   69	73	86	java/io/IOException
  }

  // ERROR //
  public android.os.MemoryFile getFile(String paramString)
  {
    // Byte code:
    //   0: iconst_1
    //   1: newarray int
    //   3: astore_2
    //   4: invokestatic 26	miui/content/res/ThemeResources:getSystem	()Lmiui/content/res/ThemeResources;
    //   7: aload_1
    //   8: aload_2
    //   9: invokevirtual 30	miui/content/res/ThemeResources:getAwesomeLockscreenFileStream	(Ljava/lang/String;[I)Ljava/io/InputStream;
    //   12: astore_3
    //   13: aload_3
    //   14: ifnull +86 -> 100
    //   17: ldc 64
    //   19: newarray byte
    //   21: astore 5
    //   23: new 66	android/os/MemoryFile
    //   26: dup
    //   27: aconst_null
    //   28: aload_2
    //   29: iconst_0
    //   30: iaload
    //   31: invokespecial 69	android/os/MemoryFile:<init>	(Ljava/lang/String;I)V
    //   34: astore 4
    //   36: iconst_0
    //   37: istore 6
    //   39: aload_3
    //   40: aload 5
    //   42: iconst_0
    //   43: ldc 64
    //   45: invokevirtual 73	java/io/InputStream:read	([BII)I
    //   48: istore 15
    //   50: iload 15
    //   52: ifle +25 -> 77
    //   55: aload 4
    //   57: aload 5
    //   59: iconst_0
    //   60: iload 6
    //   62: iload 15
    //   64: invokevirtual 77	android/os/MemoryFile:writeBytes	([BIII)V
    //   67: iload 6
    //   69: iload 15
    //   71: iadd
    //   72: istore 6
    //   74: goto -35 -> 39
    //   77: aload 4
    //   79: invokevirtual 81	android/os/MemoryFile:length	()I
    //   82: istore 16
    //   84: iload 16
    //   86: ifle +10 -> 96
    //   89: aload_3
    //   90: invokevirtual 49	java/io/InputStream:close	()V
    //   93: aload 4
    //   95: areturn
    //   96: aload_3
    //   97: invokevirtual 49	java/io/InputStream:close	()V
    //   100: aconst_null
    //   101: astore 4
    //   103: goto -10 -> 93
    //   106: astore 12
    //   108: ldc 51
    //   110: aload 12
    //   112: invokevirtual 55	java/lang/OutOfMemoryError:toString	()Ljava/lang/String;
    //   115: invokestatic 61	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   118: pop
    //   119: aload_3
    //   120: invokevirtual 49	java/io/InputStream:close	()V
    //   123: goto -23 -> 100
    //   126: astore 14
    //   128: goto -28 -> 100
    //   131: astore 9
    //   133: ldc 51
    //   135: aload 9
    //   137: invokevirtual 82	java/io/IOException:toString	()Ljava/lang/String;
    //   140: invokestatic 61	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   143: pop
    //   144: aload_3
    //   145: invokevirtual 49	java/io/InputStream:close	()V
    //   148: goto -48 -> 100
    //   151: astore 11
    //   153: goto -53 -> 100
    //   156: astore 7
    //   158: aload_3
    //   159: invokevirtual 49	java/io/InputStream:close	()V
    //   162: aload 7
    //   164: athrow
    //   165: astore 18
    //   167: goto -74 -> 93
    //   170: astore 17
    //   172: goto -72 -> 100
    //   175: astore 8
    //   177: goto -15 -> 162
    //
    // Exception table:
    //   from	to	target	type
    //   23	84	106	java/lang/OutOfMemoryError
    //   119	123	126	java/io/IOException
    //   23	84	131	java/io/IOException
    //   144	148	151	java/io/IOException
    //   23	84	156	finally
    //   108	119	156	finally
    //   133	144	156	finally
    //   89	93	165	java/io/IOException
    //   96	100	170	java/io/IOException
    //   158	162	175	java/io/IOException
  }

  public Element getManifestRoot()
  {
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    try
    {
      localElement = localDocumentBuilderFactory.newDocumentBuilder().parse(ThemeResources.getSystem().getAwesomeLockscreenFileStream("manifest.xml", null)).getDocumentElement();
      Log.i("LockScreenResourceLoader", "root:" + localElement.getNodeName());
      boolean bool = localElement.getNodeName().equals("Lockscreen");
      if (bool)
        return localElement;
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      while (true)
      {
        localParserConfigurationException.printStackTrace();
        Element localElement = null;
      }
    }
    catch (SAXException localSAXException)
    {
      while (true)
        localSAXException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.LockScreenResourceLoader
 * JD-Core Version:    0.6.0
 */