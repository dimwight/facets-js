package fjs;
import java.util.List;
import fjs.SelectingSurface.TextContent;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.util.Titled;
import fjs.util.Tracer;
public class IndexableType implements Titled{
	final public static IndexableType 
		Standard=new IndexableType("Standard"),
		ShowChars=new IndexableType("ShowChars"),
		Chooser=new IndexableType("Chooser"),
		values[]={Standard,ShowChars,Chooser};
	private final String title;
	protected IndexableType(String title){
		this.title=title;
	}
	public String titleTail(){
		return this==ShowChars?SelectingTitles.TAIL_SHOW_CHARS:"";
	}
	@Override
	public String title(){
		return title;
	}
	public static IndexableType getContentType(TextContent content){
		return content.text.length()>20?ShowChars:Standard;
	}
}