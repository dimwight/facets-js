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
	static ContentType getType(TextContent tc){
		return tc.text.length()>20?ShowChars:Standard;
	}
	public static ContentType getIndexedType(Facets facets){
		return getType((TextContent)facets.getIndexingState(SelectingTitles.TITLE_SELECT).indexed);
	}
	public String titleTail(){
		return this==ShowChars?SelectingTitles.TAIL_SHOW_CHARS:"";
	}
	@Override
	public String title(){
		return title;
	}
	public static void updateTargeterTree(List<TextContent>contents,Facets facets,Tracer t){
		for(ContentType value:values)
			for(TextContent tc:contents)if(getType(tc)==value){
				t.trace("> Updating targeters for ",value);
				facets.updateTargetState(SelectingTitles.TITLE_SELECT,contents.indexOf(tc));
				facets.updateTargeterTree();
				break;
			}
	}
}