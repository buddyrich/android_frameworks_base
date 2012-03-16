package miui.widget;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DataCache<K, V> extends LinkedHashMap<K, V>
{
  private static final long serialVersionUID = 1L;
  private int mMaximumCapacity;

  public DataCache()
  {
  }

  public DataCache(int paramInt)
  {
    super(paramInt);
  }

  public DataCache(int paramInt, float paramFloat)
  {
    super(paramInt, paramFloat);
  }

  public DataCache(int paramInt, float paramFloat, boolean paramBoolean)
  {
    super(paramInt, paramFloat, paramBoolean);
  }

  public DataCache(Map<? extends K, ? extends V> paramMap)
  {
    super(paramMap);
  }

  public int getMaximumCapacity()
  {
    return this.mMaximumCapacity;
  }

  protected boolean removeEldestEntry(Map.Entry<K, V> paramEntry)
  {
    if (size() > this.mMaximumCapacity);
    for (int i = 1; ; i = 0)
      return i;
  }

  public void setMaximumCapacity(int paramInt)
  {
    this.mMaximumCapacity = paramInt;
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.widget.DataCache
 * JD-Core Version:    0.6.0
 */