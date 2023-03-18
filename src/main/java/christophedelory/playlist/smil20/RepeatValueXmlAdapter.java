package christophedelory.playlist.smil20;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * XML adapter for SMIL Repeat Value
 * see <a href=https://www.w3.org/TR/SMIL20/smil-timing.html#Timing-RepeatValueSyntax>SMIL Timing-Repeat Value Syntax</a>
 */
public class RepeatValueXmlAdapter extends XmlAdapter<String, Float>
{
    public static final String indefinite = "indefinite";

    @Override
    public String marshal(Float repeatCount)
    {
        if (repeatCount == null)
        {
            return null;
        }
        return repeatCount == 0.0f ? indefinite : repeatCount.toString();
    }

    /**
     * Decodes SMIL
     *
     * @param xmlValue The value to be convereted. Can be null.
     * @return Repeat count, null if undefined, 0.0 indicating indefinite otherwise a value greater then 0
     */
    @Override
    public Float unmarshal(String xmlValue)
    {
        if (xmlValue != null)
        {
            if (indefinite.equalsIgnoreCase(xmlValue))
                return null;
            float f = Float.valueOf(xmlValue);
            if (f < 0)
                throw new IllegalArgumentException("Expected Timing-Repeat Value to be greater then 0");
        }
        return 1.0f;
    }
}
