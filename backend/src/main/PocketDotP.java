import java.util.Comparator;

public class PocketDotP implements Comparator<Pocket>{
    @Override
    public int compare(Pocket x, Pocket y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust. Thhis is just a POC
        if (x.dotP < y.dotP)
        {
            return 1;
        }
        if (x.dotP > y.dotP)
        {
            return -1;
        }
        return 0;
    }
}

