package fjs;
import java.util.List;
import fjs.SelectingSurface.TextContent;
import fjs.globals.Facets;
import fjs.util.Titled;
import fjs.util.Tracer;
public class ContentType implements Titled{
	final public static ContentType 
		Standard=new ContentType("Standard"),
		ShowChars=new ContentType("ShowChars"),
		values[]={Standard,ShowChars};
	public final String title;
	private ContentType(String title){
		this.title=title;
	}
	public static ContentType getIndexedType(Facets facets){
		return ((TextContent)facets.getIndexingState(SelectingTitles.TITLE_SELECT).indexed
				).text.length()>20?ShowChars:Standard;
	}
	public String titleTail(){
		return this==ShowChars?SelectingTitles.TAIL_SHOW_CHARS:"";
	}
	@Override
	public String title(){
		return title;
	}
}