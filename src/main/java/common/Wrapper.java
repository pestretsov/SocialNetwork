package common;

/**
 * Created by artemypestretsov on 8/7/16.
 */
@FunctionalInterface
public interface Wrapper<T> {
    @Private
    T toSrc();
}
