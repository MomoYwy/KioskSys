package shared.models;

public interface Recordable {
    String getId();
    String toCsvString();
}