package bci.util.work.works;

import java.util.Collection;
import java.util.stream.Collectors;
import bci.util.work.Category;
import bci.util.work.Creator;
import bci.util.work.Work;

public class Book extends Work {
    private String _ISBN;
    private Collection<Creator> _creators;

    public Book(int wid, String title, int copies, int price, Category category, Collection<Creator> creators,
            String ISBN) {
        super(wid, title, copies, price, category);
        _creators = creators;
        _ISBN = ISBN;
    }

    public String getType() {
        return "Livro";
    }

    @Override
    public String toString(){
        return super.toString() + " - " + _ISBN;
    }

    /**
     * Generates a String representation of this book's each creator, separated by commas.
     * 
     * @return The String representation.
     */
    protected String getCreatorString() {
        return _creators.stream().map(Creator::toString).collect(Collectors.joining("; "));
    }

    public Collection<String> getCreatorStrings(){
        return _creators.stream().map(Creator::toString).toList();
    }

    public Collection<Creator> getCreators(){
        return _creators;
    }

}
