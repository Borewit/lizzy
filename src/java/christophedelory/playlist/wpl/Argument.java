/*
 * Copyright (c) 2008, Christophe Delory
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY CHRISTOPHE DELORY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CHRISTOPHE DELORY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package christophedelory.playlist.wpl;

/**
 * Contains one portion of a condition string.
 * A condition string typically has a condition portion and a value portion.
 * For example, in the condition string "Artist Equals Joe", the condition portion is "Equals" and the value portion is "Joe".
 * <br>
 * When the name attribute of a fragment element is a media item characteristic such as Album Artist, or Genre,
 * the fragment element must contain two argument elements: one that specifies a condition and another that specifies a value.
 * The following table shows two possible values for the name attribute and how argument elements are used to specify conditions and values:
 * <table>
 * <tr>
 * <th>Attribute value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>Condition</td>
 * <td>The content of the argument element is the condition portion of a condition string.
 * For example, in the condition string "Artist Equals Joe", the condition portion is "Equals".
 * The condition portion of a condition string must be one of the following:
 * Equals, Does Not Equal, Contains, Does Not Contain, Is Less Than, Is Greater Than, Is, Is Not, Is Before, Is Later Than, Is More Recent Than, Above, Below, Ascending, Descending, Random, Is At Least, Is No More Than.</td>
 * </tr>
 * <tr>
 * <td>Value</td>
 * <td>The content of the argument element is the value portion of a condition string.
 * For example, in the condition string "Artist Equals Joe", the value portion is "Joe".
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Artist"&gt;
 *  &lt;argument name = "Condition"&gt;Equals&lt;/argument&gt;
 *  &lt;argument name = "Value"&gt;Joe&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * </td>
 * </tr>
 * </table>
 * When the name attribute of a fragment element is "Limit Total Size To" or "Limit Total Duration To",
 * the fragment element must contain two argument elements: one that specifies a format and one that specifies a number.
 * The following table shows two possible values for the name attribute and how argument elements are used to limit the size or duration of a playlist:
 * <table>
 * <tr>
 * <th>Attribute value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>Format</td>
 * <td>When the name attribute of the fragment element is "Limit Total Size To", the content of the argument element must be one of the following:
 * Kilobytes, Megabytes, or Gigabytes.
 * <br>
 * When the name attribute of the fragment element is "Limit Total Duration To", the content of the argument element must be one of the following:
 * Seconds, Minutes, Hours, or Days.</td>
 * </tr>
 * <tr>
 * <td>Number</td>
 * <td>The content of the argument element is a number that limits the size or duration of the playlist.
 * <br>
 * Examples:
 * <pre>
 * &lt;fragment name = "Limit Total Size To"&gt;
 *  &lt;argument name = "Format"&gt;Megabytes&lt;/argument&gt;
 *  &lt;argument name = "Number"&gt;5&lt;/argument&gt;
 * &lt;/fragment&gt;
 *
 * &lt;fragment name = "Limit Total Duration To"&gt;
 *  &lt;argument name = "Format"&gt;Minutes&lt;/argument&gt;
 *  &lt;argument name = "Number"&gt;20&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre></td>
 * </tr>
 * </table>
 * When the name attribute of a fragment element is "Limit Number of Items",
 * the fragment element must contain one argument element that specifies the number of items.
 * The following table shows how to use the Number value of the name attribute:
 * <table>
 * <tr>
 * <th>Attribute value</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>Number</td>
 * <td>The content of the argument element is a number that limits the number of items in a playlist.
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Limit Number of Items"&gt;
 *  &lt;argument name = "Number"&gt;15&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre></td>
 * </tr>
 * </table>
 * Windows Argument Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="argument"
 */
public class Argument
{
    /**
     * The name of one portion of the condition string.
     */
    private String _name = "";

    /**
     * The content of the argument.
     */
    private String _content = "";

    /**
     * Returns the name of one portion of the condition string.
     * @return the argument name. Shall not be <code>null</code>.
     * @see #setName
     * @castor.field
     *  get-method="getName"
     *  set-method="setName"
     *  required="true"
     * @castor.field-xml
     *  name="name"
     *  node="attribute"
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Initializes the name of one portion of the condition string.
     * @param name the argument name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }

    /**
     * Returns the content of this argument.
     * @return the argument's content. Shall not be <code>null</code>.
     * @see #setContent
     * @castor.field
     *  get-method="getContent"
     *  set-method="setContent"
     *  required="true"
     * @castor.field-xml
     *  node="text"
     */
    public String getContent()
    {
        return _content;
    }

    /**
     * Initializes the content of this argument.
     * @param content the argument's content. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>content</code> is <code>null</code>.
     * @see #getContent
     */
    public void setContent(final String content)
    {
        _content = content.trim(); // Throws NullPointerException if content is null.
    }
}
