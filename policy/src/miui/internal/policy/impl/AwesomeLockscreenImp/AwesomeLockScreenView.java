package com.miui.internal.policy.impl.AwesomeLockScreenImp;

import android.content.Context;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import miui.app.screenelement.IndexedNumberVariable;

public class AwesomeLockScreenView extends View
{
  private static final boolean CALCULATE_FRAME_RATE = true;
  private static final String LOG_TAG = "AwesomeLockScreenView";
  private static final int MOVING_THRESHOLD = 25;
  private LockScreenRoot.UnlockerCallback mCallback;
  private int mFrameRate = 30;
  private int mFrames;
  private boolean mPaused = true;
  private long mPausedTime;
  private LockScreenRoot mRoot;
  private boolean mStarted;
  private boolean mStop;
  private RenderThread mThread;

  public AwesomeLockScreenView(Context paramContext, LockScreenRoot.UnlockerCallback paramUnlockerCallback, LockScreenRoot paramLockScreenRoot)
  {
    super(paramContext);
    this.mCallback = paramUnlockerCallback;
    setFocusable(true);
    setFocusableInTouchMode(true);
    this.mFrameRate = paramLockScreenRoot.getFrameRate();
    this.mRoot = paramLockScreenRoot;
    this.mRoot.setView(this);
  }

  public void cleanUp()
  {
    setOnTouchListener(null);
    this.mStop = true;
    onResume();
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (this.mThread == null)
    {
      this.mThread = new RenderThread();
      this.mThread.start();
    }
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (!this.mStarted)
      return;
    try
    {
      synchronized (this.mRoot)
      {
        this.mRoot.tick(SystemClock.elapsedRealtime() - this.mPausedTime);
        this.mRoot.render(paramCanvas);
        this.mFrames = (1 + this.mFrames);
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        Log.e("AwesomeLockScreenView", localException.toString());
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      while (true)
      {
        localOutOfMemoryError.printStackTrace();
        Log.e("AwesomeLockScreenView", localOutOfMemoryError.toString());
      }
    }
  }

  public void onPause()
  {
    this.mPaused = true;
  }

  public void onResume()
  {
    this.mPaused = false;
    if (this.mThread != null)
      this.mThread.onResume();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (this.mThread != null)
      this.mThread.onTouch(paramMotionEvent);
    return true;
  }

  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if ((paramInt == 0) && (this.mThread != null))
      this.mThread.onResume();
  }

  private class RenderThread extends Thread
  {
    private IndexedNumberVariable mFrameRateVar;
    private int mFrameTime;
    private long mLastTouchTime;
    private long mLastUpdateSystemTime;
    private int mPreX;
    private int mPreY;
    private Object notify = new Object();

    public RenderThread()
    {
      if (AwesomeLockScreenView.this.mFrameRate != 0);
      for (int i = 1000 / AwesomeLockScreenView.this.mFrameRate; ; i = 2147483647)
      {
        this.mFrameTime = i;
        return;
      }
    }

    private void waiteForResume()
    {
      try
      {
        synchronized (this.notify)
        {
          this.notify.wait();
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }

    public void onResume()
    {
      if ((!AwesomeLockScreenView.this.mPaused) && (AwesomeLockScreenView.this.getVisibility() == 0))
        synchronized (this.notify)
        {
          this.notify.notify();
        }
    }

    public void onTouch(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getActionMasked();
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      if (i == 2)
        if (SystemClock.elapsedRealtime() - this.mLastTouchTime >= 1000L)
        {
          int m = j - this.mPreX;
          int n = k - this.mPreY;
          if (m * m + n * n > 25)
          {
            AwesomeLockScreenView.this.mCallback.pokeWakelock();
            this.mLastTouchTime = SystemClock.elapsedRealtime();
            this.mPreX = j;
            this.mPreY = k;
          }
        }
      try
      {
        synchronized (AwesomeLockScreenView.this.mRoot)
        {
          try
          {
            do
            {
              AwesomeLockScreenView.this.mRoot.onTouch(paramMotionEvent);
              AwesomeLockScreenView.this.mRoot.setShouldUpdate(true);
              return;
            }
            while (i != 0);
            this.mPreX = j;
            this.mPreY = k;
          }
          catch (Exception localException)
          {
            while (true)
            {
              localException.printStackTrace();
              Log.e("AwesomeLockScreenView", localException.toString());
            }
          }
        }
      }
      catch (OutOfMemoryError localOutOfMemoryError)
      {
        while (true)
        {
          localOutOfMemoryError.printStackTrace();
          Log.e("AwesomeLockScreenView", localOutOfMemoryError.toString());
        }
      }
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: ldc 115
      //   2: ldc 130
      //   4: invokestatic 133	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   7: pop
      //   8: aload_0
      //   9: new 135	miui/app/screenelement/IndexedNumberVariable
      //   12: dup
      //   13: ldc 137
      //   15: aload_0
      //   16: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   19: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   22: invokevirtual 141	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:getContext	()Lmiui/app/screenelement/ScreenContext;
      //   25: getfield 147	miui/app/screenelement/ScreenContext:mVariables	Lmiui/app/screenelement/Variables;
      //   28: invokespecial 150	miui/app/screenelement/IndexedNumberVariable:<init>	(Ljava/lang/String;Lmiui/app/screenelement/Variables;)V
      //   31: putfield 152	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:mFrameRateVar	Lmiui/app/screenelement/IndexedNumberVariable;
      //   34: lconst_0
      //   35: lstore_2
      //   36: aload_0
      //   37: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   40: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   43: invokevirtual 155	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:init	()V
      //   46: aload_0
      //   47: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   50: lconst_0
      //   51: invokestatic 159	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$302	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;J)J
      //   54: pop2
      //   55: aload_0
      //   56: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   59: iconst_1
      //   60: invokestatic 163	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$402	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;Z)Z
      //   63: pop
      //   64: aload_0
      //   65: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   68: invokevirtual 166	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:postInvalidate	()V
      //   71: aload_0
      //   72: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   75: invokestatic 169	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$500	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Z
      //   78: ifne +119 -> 197
      //   81: aload_0
      //   82: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   85: invokestatic 53	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$600	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Z
      //   88: ifne +13 -> 101
      //   91: aload_0
      //   92: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   95: invokevirtual 57	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:getVisibility	()I
      //   98: ifeq +85 -> 183
      //   101: aload_0
      //   102: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   105: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   108: invokevirtual 172	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:pause	()V
      //   111: ldc 115
      //   113: ldc 174
      //   115: invokestatic 133	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   118: pop
      //   119: invokestatic 83	android/os/SystemClock:elapsedRealtime	()J
      //   122: lstore 15
      //   124: aload_0
      //   125: invokespecial 176	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:waiteForResume	()V
      //   128: aload_0
      //   129: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   132: invokestatic 83	android/os/SystemClock:elapsedRealtime	()J
      //   135: lload 15
      //   137: lsub
      //   138: invokestatic 179	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$314	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;J)J
      //   141: pop2
      //   142: ldc 115
      //   144: new 181	java/lang/StringBuilder
      //   147: dup
      //   148: invokespecial 182	java/lang/StringBuilder:<init>	()V
      //   151: ldc 184
      //   153: invokevirtual 188	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   156: aload_0
      //   157: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   160: invokestatic 192	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$300	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)J
      //   163: invokevirtual 195	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
      //   166: invokevirtual 196	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   169: invokestatic 133	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   172: pop
      //   173: aload_0
      //   174: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   177: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   180: invokevirtual 199	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:resume	()V
      //   183: aload_0
      //   184: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   187: invokestatic 169	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$500	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Z
      //   190: istore 20
      //   192: iload 20
      //   194: ifeq +22 -> 216
      //   197: aload_0
      //   198: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   201: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   204: invokevirtual 202	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:finish	()V
      //   207: ldc 115
      //   209: ldc 204
      //   211: invokestatic 133	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   214: pop
      //   215: return
      //   216: invokestatic 83	android/os/SystemClock:elapsedRealtime	()J
      //   219: lstore 21
      //   221: lload_2
      //   222: lconst_0
      //   223: lcmp
      //   224: ifne +104 -> 328
      //   227: lload 21
      //   229: lstore_2
      //   230: lload 21
      //   232: aload_0
      //   233: getfield 206	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:mLastUpdateSystemTime	J
      //   236: lsub
      //   237: aload_0
      //   238: getfield 38	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:mFrameTime	I
      //   241: i2l
      //   242: lcmp
      //   243: ifgt +16 -> 259
      //   246: aload_0
      //   247: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   250: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   253: invokevirtual 210	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:shouldUpdate	()Z
      //   256: ifeq +27 -> 283
      //   259: aload_0
      //   260: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   263: invokevirtual 166	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:postInvalidate	()V
      //   266: aload_0
      //   267: lload 21
      //   269: putfield 206	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:mLastUpdateSystemTime	J
      //   272: aload_0
      //   273: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   276: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   279: iconst_0
      //   280: invokevirtual 112	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:setShouldUpdate	(Z)V
      //   283: aload_0
      //   284: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   287: invokestatic 104	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$200	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot;
      //   290: invokevirtual 213	com/miui/internal/policy/impl/AwesomeLockScreenImp/LockScreenRoot:getFrameRate	()I
      //   293: bipush 60
      //   295: if_icmpge -224 -> 71
      //   298: ldc2_w 214
      //   301: invokestatic 219	java/lang/Thread:sleep	(J)V
      //   304: goto -233 -> 71
      //   307: astore 9
      //   309: aload 9
      //   311: invokevirtual 113	java/lang/Exception:printStackTrace	()V
      //   314: ldc 115
      //   316: aload 9
      //   318: invokevirtual 119	java/lang/Exception:toString	()Ljava/lang/String;
      //   321: invokestatic 125	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   324: pop
      //   325: goto -128 -> 197
      //   328: lload 21
      //   330: lload_2
      //   331: lsub
      //   332: lstore 23
      //   334: lload 23
      //   336: ldc2_w 86
      //   339: lcmp
      //   340: iflt -110 -> 230
      //   343: sipush 1000
      //   346: aload_0
      //   347: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   350: invokestatic 222	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$700	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;)I
      //   353: imul
      //   354: i2l
      //   355: lload 23
      //   357: ldiv
      //   358: lstore 25
      //   360: aload_0
      //   361: getfield 152	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:mFrameRateVar	Lmiui/app/screenelement/IndexedNumberVariable;
      //   364: lload 25
      //   366: l2d
      //   367: invokevirtual 226	miui/app/screenelement/IndexedNumberVariable:set	(D)V
      //   370: aload_0
      //   371: getfield 24	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView$RenderThread:this$0	Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;
      //   374: iconst_0
      //   375: invokestatic 230	com/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView:access$702	(Lcom/miui/internal/policy/impl/AwesomeLockScreenImp/AwesomeLockScreenView;I)I
      //   378: pop
      //   379: lload 21
      //   381: lstore_2
      //   382: goto -152 -> 230
      //   385: astore 4
      //   387: aload 4
      //   389: invokevirtual 126	java/lang/OutOfMemoryError:printStackTrace	()V
      //   392: ldc 115
      //   394: aload 4
      //   396: invokevirtual 127	java/lang/OutOfMemoryError:toString	()Ljava/lang/String;
      //   399: invokestatic 125	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   402: pop
      //   403: goto -206 -> 197
      //   406: astore 6
      //   408: aload 6
      //   410: invokevirtual 113	java/lang/Exception:printStackTrace	()V
      //   413: ldc 115
      //   415: aload 6
      //   417: invokevirtual 119	java/lang/Exception:toString	()Ljava/lang/String;
      //   420: invokestatic 125	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   423: pop
      //   424: goto -217 -> 207
      //
      // Exception table:
      //   from	to	target	type
      //   36	192	307	java/lang/Exception
      //   216	304	307	java/lang/Exception
      //   343	379	307	java/lang/Exception
      //   36	192	385	java/lang/OutOfMemoryError
      //   216	304	385	java/lang/OutOfMemoryError
      //   343	379	385	java/lang/OutOfMemoryError
      //   197	207	406	java/lang/Exception
    }
  }
}

/* Location:           /home/dhacker29/miui/android.policy_dex2jar.jar
 * Qualified Name:     com.miui.internal.policy.impl.AwesomeLockScreenImp.AwesomeLockScreenView
 * JD-Core Version:    0.6.0
 */