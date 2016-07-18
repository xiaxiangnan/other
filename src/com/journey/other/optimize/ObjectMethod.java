package com.journey.other.optimize;

/**
 * equals hashCode
 * Created by xiaxiangnan on 16/7/7.
 */
public class ObjectMethod implements Cloneable {

    private boolean b;

    private short s;

    private long l;

    private float d;

    /**
     * 重写equals后,一定要重写hashCode
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ObjectMethod)) {
            return false;
        }
        ObjectMethod tempObj = (ObjectMethod) obj;
        return b == tempObj.b && s == tempObj.s && l == tempObj.l && d == tempObj.d;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + (b ? 1 : 0); // boolean
        result = result * 31 + (int) s; // byte char short int
        result = result * 31 + (int) (l ^ (l >>> 32)); // long
        result = result * 31 + Float.floatToIntBits(d); // float double(Double.doubleToLongBits然后按long转)
        return result;
    }

    /**
     * 此类实现了 Cloneable 接口，以指示 Object.clone() 方法可以合法地对该类实例进行按字段复制
     * 如果在没有实现 Cloneable 接口的实例上调用 Object 的 clone 方法，
     * 则会导致抛出 CloneNotSupportedException 异常。
     * 浅拷贝
     */
    @Override
    public ObjectMethod clone() throws CloneNotSupportedException {
        return (ObjectMethod) super.clone();
    }
}
