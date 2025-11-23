package CustomUIElements;

@FunctionalInterface
public interface CardFactory<T> {
    BaseCard<T> createCard(T data, String flavour);
}