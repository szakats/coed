/**
 * 
 */
package coed.plugin.base;

import java.util.ArrayList;

import org.eclipse.core.internal.filebuffers.SynchronizableDocument;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import coed.base.data.CoedFileLine;

/**
 * Class which offers helper methods for conversion from CoedFileLines to
 * portions of an IDocument, and vice versa.
 * @author Izso
 *
 */
public class TextLinesHelper {

	/**
	 * Returns the array of lines that should be locked for editing
	 * @param doc Document on which one works
	 * @param eOffset Offset of the event
	 * @param eLenght Length of the event
	 * @return An array containing the indexes of the lines
	 */
	public static Integer[] getAffectedLines(IDocument doc, Integer eOffset, Integer eLenght){
		ArrayList<Integer> lines = new ArrayList<Integer>();
		try {
			Integer firstLine = doc.getLineOfOffset(eOffset);
			Integer firstLineOffs = doc.getLineOffset(firstLine);
			Integer firstLineLength = doc.getLineLength(firstLine);
			Integer remainLength = eLenght-firstLineLength;
			
			lines.add(firstLine);
			
			Integer nextLine=firstLine;
			while (remainLength>0) {
				nextLine++;
				lines.add(nextLine);
				remainLength-=doc.getLineLength(nextLine);
			}
			Integer[] rez = new Integer[0];
			rez=lines.toArray(rez);
			return rez;
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts a portion of the document into an array of CoedFileLines, whoch can be sent as changes.
	 * Values are taken from an event object 
	 * @param doc
	 * @param eOffset event.fOffset
	 * @param eLenght event.fLength
	 * @param eText event.fText
	 * @return
	 */
	public static CoedFileLine[] getCoedFileLines(IDocument doc, Integer eOffset, Integer eLenght, String eText) {	
		Integer[] lines = getAffectedLines(doc, eOffset, eLenght);
		return getCoedFileLines(doc, lines, eText);
	}
	
	/**
	 * Converts a portion of the document into an array of CoedFileLines, whoch can be sent as changes.
	 * Values are taken from an event object 
	 * @param actualLines
	 * @param eText
	 * @return
	 */
	public static CoedFileLine[] getCoedFileLines(IDocument doc, Integer[] actualLines, String eText) {
		CoedFileLine[] lines =  new CoedFileLine[actualLines.length];
		String[] sta = eText.split("\n");
		try {
			sta[0] = doc.get(doc.getLineOffset(actualLines[0]),doc.getLineLength(actualLines[0])-1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		lines[0] = new CoedFileLine(actualLines[0], sta, "line.ins");
		int tot = actualLines.length;
		for (int j=1; j<actualLines.length; j++) {
			lines[tot-j]=new CoedFileLine(actualLines[j], null, "line.del");
		}
		
		return lines;
	}
	
	/**
	 * Executes as replace operation of IDocument the given CoedFileLine
	 * @param doc
	 * @param fline
	 * @return
	 */
	public static boolean doAsReplace(IDocument doc, CoedFileLine fline){
		try {
			Integer lineNum = fline.getLine();
			Integer lineOffs = doc.getLineOffset(lineNum);
			Integer lineLength = doc.getLineLength(lineNum);
			doc.replace(lineOffs, lineLength, stringArrayJoin(fline.getText(), "\n"));
			return true;
		} catch (BadLocationException e) {
			return false;
		}
	}
	
	private static String stringArrayJoin(String[] sa, String joinToken) {
		String res = "";
		for (int j=0; j<sa.length-1; j++) {
			res+=sa[j]+joinToken;
		}
		res+=sa[sa.length-1];
		
		return res;
	}
}
