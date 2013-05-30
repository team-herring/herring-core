package org.herring.core.cruiser.model;
import java.io.Serializable;

/**
 *
 * @author Youngdeok Kim
 * @since 1.0
 */
public class HerringCruiserCommand implements Serializable {
    private static final long serialVersionUID = -2954176501909939119L;
    public static final int SEND_OBJECT = 1;
    public static final int SEND_BYTES = 2;
}
