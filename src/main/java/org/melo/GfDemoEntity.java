package org.melo;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;

/**
 * Created by CSCOLLO on 15.11.2016.
 */

@Region("Values")
public class GfDemoEntity {

    @Id
    String key;

    String value;

}
