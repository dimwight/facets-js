package fjs.swing.html;
import static facets.util.Regex.find;
import static facets.util.Regex.replaceAll;
import facets.util.HtmlBuilder;
import facets.util.Regex;
import facets.util.HtmlFormBuilder.FormInput;
public abstract class HtmlLayoutBuilder extends HtmlBuilder{
	private static final boolean debugText=false;
	public static final class FormInput{
		public final String id,value;
		public FormInput(String id,String value){
			this.id=id;
			this.value=value;
		}
	}
	protected String newDisplayText(String id,Object value){
		String text=value.toString();
		return text.startsWith("<a href=")?text
				:Regex.replaceAll(text,"<","&lt;",">","&gt;");
	}
	private enum FormTag{
		InputText,InputCheckbox,TextArea,Select;
		public String findValue(String code,String id){
			boolean area=this==TextArea;
			String _nameToEnd="[^>]*id='"+id+"'[^>]*>",
					tag=find(code,area?"<textarea"+_nameToEnd+"[^<]*<"
							:((this==Select?"<select":"<input")+_nameToEnd));
			if(tag.equals(""))
				throw new IllegalStateException("No "+this+" with id="+id);
			String value=Regex.replaceAll(tag,
					area?"<textarea[^>]+(>[^<]*<)":".*value=([^>]+).*","$1","&lt;","<",
					"&gt;",">");
			int chars=value.length();
			return chars==1?"":value.substring(1,chars-1);
		}
		public static String newInputText(String id,int size,String value){
			if(debugText) size=30;
			boolean singles=value.indexOf("'")>-1,doubles=value.indexOf('"')>-1;
			if(singles&doubles){
				if(false)
					throw new IllegalArgumentException("Can't quote value="+value);
				else value=value.replace("\"","'");
			}
			String quote=singles?"\"":"'";
			return "\n<input type='text' id='"+id+"' size='"
					+(value.trim().equals("")?1:size)+"' value="+quote
					+Regex.replaceAll(value,"<","&lt;",">","&gt;")+quote+">\n";
		}
		public static String newTextArea(String id,int cols,int rows,
				String value){
			if(debugText) cols=30;
			return "\n<textarea wrap='virtual' rows='"+rows+"'"+" cols='"
					+(value.trim().equals("")?1:cols)+"' id='"+id+"'>"+value
					+"</textarea>\n";
		}
		public static String newInputCheckbox(String id,String value){
			return "\n<input type='checkbox' id='"+id+"' value='"+value+"'>\n";
		}
		public static String newSelect(String id,String[] options,String value){
			StringBuilder code=new StringBuilder(
					"\n<select id='"+id+"' value='"+value+"'>\n");
			for(String option:options)
				code.append("<option>"+option+"</option>\n");
			return code.toString()+"</select>\n";
		}
		public static FormTag getTag(String id,String inputType){
			return id.equals("textarea")?TextArea
					:id.equals("select")?Select
							:inputType.equals("checkbox")?InputCheckbox:InputText;
		}
	}
	final private static boolean boxBorders=false;
	final private static int cellBorder=0;
	private final String[]idRows;
	public HtmlLayoutBuilder(String...idRows){
		super(RenderTarget.Swing);
		this.idRows=idRows;
	}
	@Override
	public String newPageContent(){
		return buildForm();
	}
	public final String buildForm(){
		StringBuilder form=new StringBuilder();
		form.append("<table border=0 cellspacing=3 cellpadding=0>");
		for(String idRow:idRows){
			StringBuilder row=new StringBuilder("<tr><td><table><tr>\n");
			for(String id:idRow.split(",")){
				Object value=getValue(id);
				if(value==null)
					throw new IllegalStateException("Null value for id="+id);
				else row.append(isNullField(id,value)&&hideNullField(id,value)?""
						:"<td><table cellspacing=3 cellpadding=0 border="+cellBorder+">\n"
								+"<tr"+(false&&isTextArea(id)?" valign=top":"")+"><td border="
								+cellBorder+"><b>"+getTitle(id).replace("<","&lt;")+"</b></td><td>"
								+(useInputField(id)?newFieldCode(id,value)
										:"\n<table class=\"box\" cellspacing=0 border="
												+(boxBorders?1:0)+" cellpadding="
												+(boxBorders&&!isTextArea(id)?5:0)+">\n"
												+"<tr><td>&nbsp;"+newDisplayText(id,value)
												+"&nbsp;</td></tr></table>\n")
								+"</td></tr>\n</table></td>");
			}
			row.append("</tr></table></td></tr>\n");
			form.append(row.toString());
		}
		form.append("</table>");
		return form.toString();
	}
	protected String getTitle(String id){
		return id;
	}
	protected boolean isNullField(String id,Object value){
		return value.toString().trim().equals("");
	}
	protected abstract Object getValue(String id);
	protected boolean hideNullField(String id,Object value){
		return false;
	}
	protected boolean isTextArea(String id){
		return false;
	}
	protected boolean useInputField(String id){
		return true;
	}
	protected String newFieldCode(String id,Object value){
		String text=value.toString();
		return FormTag.newInputText(id,inputCols(id),text);
	}
	protected int inputCols(String id){
		return 10;
	}
	public void readEdit(FormInput edit){
		throw new RuntimeException("Not implemented for "+edit);
	}
}