package fjs;
import java.util.List;
import fjs.SelectingSurface.TextContent;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.util.Titled;
import fjs.util.Tracer;
public class SelectableType implements Titled{
	final public static SelectableType 
		Standard=new SelectableType("Standard"),
		ShowChars=new SelectableType("ShowChars"),
		Chooser=new SelectableType("Chooser"),
		values[]={Standard,ShowChars,Chooser};
	private final String title;
	protected SelectableType(String title){
		this.title=title;
	}
	public String titleTail(){
		return this==ShowChars?SelectingTitles.TAIL_SHOW_CHARS:"";
	}
	@Override
	public String title(){
		return title;
	}
	public static SelectableType getContentType(TextContent content){
		return content.text.length()>20?ShowChars:Standard;
	}
}