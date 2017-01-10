package org.melo;

import com.gemstone.gemfire.pdx.PdxReader;
import com.gemstone.gemfire.pdx.PdxSerializable;
import com.gemstone.gemfire.pdx.PdxWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;

/**
 * Created by CSCOLLO on 15.11.2016.
 */

@Region("Objects")
public class GfDemoEntity implements PdxSerializable {

    @Id
    String key;

    public String getKey() {
        return key;
    }

    String value;

    public String getValue() {
        return value;
    }

    @Override
    public void toData(PdxWriter pdxWriter) {
        pdxWriter.writeString("key", key);
        pdxWriter.writeString("value", value);
    }

    @Override
    public void fromData(PdxReader pdxReader) {
        key = pdxReader.readString("key");
        value = pdxReader.readString("value");
    }
}