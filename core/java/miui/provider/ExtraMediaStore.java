package miui.provider;

public final class ExtraMediaStore
{
  private static final String TAG = "ExtraMediaStore";

  public static class Audio
  {
    public static abstract interface Albums
    {
      public static final String ALBUM_SORT_KEY_PRIMARY = "album_sort_key";
    }

    public static abstract interface Artists
    {
      public static final String ARTIST_SORT_KEY_PRIMARY = "artist_sort_key";
    }

    public static abstract interface Media
    {
      public static final String ALBUM_SORT_KEY_PRIMARY = "album_sort_key";
      public static final String ARTIST_SORT_KEY_PRIMARY = "artist_sort_key";
      public static final String SORT_KEY_PRIMARY = "sort_key";
    }
  }
}

/* Location:           /home/dhacker29/miui/services_dex2jar.jar
 * Qualified Name:     miui.provider.ExtraMediaStore
 * JD-Core Version:    0.6.0
 */