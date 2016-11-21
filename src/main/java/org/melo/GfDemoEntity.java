package org.melo;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;

import java.io.Serializable;

/**
 * Created by CSCOLLO on 15.11.2016.
 */

@Region("Values")
public class GfDemoEntity implements Serializable{

    @Id
    String key;

    public String getValue() {
        return value;
    }

    String value;

}
