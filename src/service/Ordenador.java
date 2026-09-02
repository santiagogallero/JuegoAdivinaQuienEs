package service;
import java.util.List;


public interface Ordenador<T> {
    List<T> ordenar(List<T> elementos);
}
