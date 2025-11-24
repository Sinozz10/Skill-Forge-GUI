package CustomUIElements;


public interface CardFactory<T> {
    GenericCard<T> createCard(T data, String flavour);
}