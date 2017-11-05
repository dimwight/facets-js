package fjs;
import java.util.List;
import fjs.SelectingSurface.TextContent;
import fjs.core.STarget;
import fjs.globals.Facets;
import fjs.util.Titled;
import fjs.util.Tracer;
public class SelectingTargetType implements Titled, SelectingTitles{
	final public static SelectingTargetType 
		Standard=new SelectingTargetType("Standard"),
		ShowChars=new SelectingTargetType("ShowChars"),
		Chooser=new SelectingTargetType("Chooser"),
		values[]={Standard,ShowChars,Chooser};
	public final String title;
	protected SelectingTargetType(String title){
		this.title=title;
	}
	public String titleTail(){
		return this==ShowChars?TAIL_SHOW_CHARS:"";
	}
	@Override
	public String title(){
		return title;
	}
}