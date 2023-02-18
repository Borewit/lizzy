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

import java.util.ArrayList;
import java.util.List;

/**
 * Specifies one condition of the query that selects items from the library.
 * Conditions are specified by condition strings.
 * A condition string typically has a name portion, a condition portion, and a value portion.
 * <br>
 * Certain condition strings have a metadata attribute portion, a condition portion, and a value portion.
 * For example, in the condition string "Album Artist Is Joe", the metadata attribute portion is "Album Artist",
 * the condition portion is "Is", and the value portion is "Joe".
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Album Artist"&gt;
 *  &lt;argument name = "condition"&gt;Is&lt;/argument&gt;
 *  &lt;argument name = "value"&gt;Joe&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * For condition strings of this type, the following table shows the possible metadata attributes, possible conditions and possible values:
 * <table>
 * <tr>
 * <th>Metadata attribute</th>
 * <th>Possible conditions</th>
 * <th>Possible values</th>
 * <tr>
 * <td>Actor, Album Artist, Album Title, Author, Caption, Channel, Composer, Conductor, Content Provider, Content Provider Genre, Contributing Artist, Copyright Text, Director, Episode, File Type, Genre, Key, Keywords, Language, Mood, Parental Rating, Period, Producer, Provider, Publisher, Series, Station name, Subgenre, Subtitle, Title, Writer</td>
 * <td>Equals, Does Not Equal, Is, Is Not, Contains, Does Not Contain</td>
 * <td>Any string value</td>
 * </tr>
 * <tr>
 * <td>Bit Rate (in kilobytes per second)</td>
 * <td>Equals, Does Not Equal, Is, Is Not, Contains, Does Not Contain</td>
 * <td>48, 64, 96, 128, 160, 192, 256, 300, 500, 750, 1000, 1500, 3000, 4500, 6000, 7500</td>
 * </tr>
 * <tr>
 * <td>Secondary Media Type</td>
 * <td>Equals, Does Not Equal, Is, Is Not, Contains, Does Not Contain</td>
 * <td>Audio: News, Audio: Talk Show, Audio: Audio Books, Audio: Audio Spoken Word, Video: News, Video: Talk Show, Video: Home Video, Video: Movie / Film, Video: TV show, Video: Corporate Video, Video: Music Video</td>
 * </tr>
 * <tr>
 * <td>File Size (in KB), Image height, Image width, Play Count : Afternoon Totals, Play Count : Evening Totals, Play Count : Morning Totals, Play Count : Night Totals, Play Count : Total Overall, Play Count : Total Weekday, Play Count : Total Weekend</td>
 * <td>Is Less Than, Is Greater Than, Is, Is Not</td>
 * <td>Any number</td>
 * </tr>
 * <tr>
 * <td>Broadcast time, Date Encoded, Date Recorded, Date taken, Release Year</td>
 * <td>Is Before, Is After, Is, Is Not</td>
 * <td>Yesterday, Last week, Last month, 6 months, 1 year, 2 years, 5 years, 2000s, 1990s, 1980s, 1970s, 1960s, 1950s, 1940s</td>
 * </tr>
 * <tr>
 * <td>Date Added</td>
 * <td>Is Before, Is After, Is, Is Not</td>
 * <td>Yesterday, Last week, Last month, 6 months, 1 year, 2 years, 5 years</td>
 * </tr>
 * <tr>
 * <td>Date Last Played</td>
 * <td>Older Than, More Recent Than, Is, Is Not</td>
 * <td>Yesterday, Last week, Last month, 6 months, 1 year, 2 years, 5 years</td>
 * </tr>
 * <tr>
 * <td>Month taken</td>
 * <td>Is Before, Is More Recent Than, Is, Is Not</td>
 * <td>1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13</td>
 * </tr>
 * <tr>
 * <td>Year taken</td>
 * <td>Is Before, Is More Recent Than, Is, Is Not</td>
 * <td>Any year</td>
 * </tr>
 * <tr>
 * <td>Auto Rating, My Rating</td>
 * <td>Is At Least, Is No More Than, Is, Is Not</td>
 * <td>Unrated, 1 Star, 2 Stars, 3 Stars, 4 Stars, 5 Stars</td>
 * </tr>
 * <tr>
 * <td>Custom Field #1, Custom Field #2, File Name, Key Fields</td>
 * <td>Contains, Does Not Contain</td>
 * <td>Any string</td>
 * </tr>
 * </table>
 * Certain condition strings have a limiter portion, a number portion and a format portion.
 * For example in the condition string "Limit Total Size To 3 Megabytes", the limiter portion is "Limit Total Size To",
 * the number portion is "3" and the format portion is "Megabytes".
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Limit Total Size To"&gt;
 *  &lt;argument name = "number"&gt;3&lt;/argument&gt;
 *  &lt;argument name = "format"&gt;Megabytes&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * For condition strings of this type the following table shows the possible limiters and formats:
 * <table>
 * <tr>
 * <th>Limiter</th>
 * <th>Possible numbers</th>
 * <th>Possible formats</th>
 * </tr>
 * <tr>
 * <td>Limit Total Size To</td>
 * <td>Any number</td>
 * <td>Kilobytes, Megabytes, Gigabytes</td>
 * </tr>
 * <tr>
 * <td>Limit Total Duration To</td>
 * <td>Any number</td>
 * <td>Seconds, Minutes, Hours, Days</td>
 * </tr>
 * </table>
 * Certain condition strings have a limiter portion and a number portion.
 * For example in the condition string "Limit Number of Items to 25", the limiter portion is "Limit Number of Items" and the number portion is "25".
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Limit Number of Items"&gt;
 *  &lt;argument name = "number"&gt;25&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * For condition strings of this type the following table shows the only possible limiter:
 * <table>
 * <tr>
 * <th>Limiter</th>
 * <th>Possible numbers</th>
 * </tr>
 * <tr>
 * <td>Limit Number Of Items</td>
 * <td>Any number</td>
 * </tr>
 * </table>
 * Certain condition strings have a protection portion and a condition portion.
 * For example in the condition string "Protection Is present", the protection portion is "Protection" and the condition portion is "Is".
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Protection"&gt;
 *  &lt;argument name = "condition"&gt;Is&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * For condition strings of this type the following table shows the possible conditions:
 * <table>
 * <tr>
 * <th>Protection portion</th>
 * <th>Possible conditions</th>
 * </tr>
 * <tr>
 * <td>Protection</td>
 * <td>Is, Is Not</td>
 * </tr>
 * </table>
 * There is one type of fragment element that does not contain a condition string.
 * If the name attribute of a fragment element is "Randomize Playback Order" the fragment element contains no argument elements.
 * This fragment element instructs the player to play the list in random order.
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Randomize Playback Order"&gt;
 * &lt;/fragment&gt;
 * </pre>
 * Certain condition strings have a sort portion, a value portion and a condition portion.
 * For example, in the condition string "Sort By Title Ascending order", the sort portion is "Sort By", the value portion is "Title",
 * and the condition portion is "Ascending".
 * Note that in this case the value portion is a metadata attribute.
 * <br>
 * Example:
 * <pre>
 * &lt;fragment name = "Sort By"&gt;
 *  &lt;argument name = "value"&gt;Title&lt;/argument&gt;
 *  &lt;argument name = "condition"&gt;Ascending&lt;/argument&gt;
 * &lt;/fragment&gt;
 * </pre>
 * For condition strings of this type the following table shows the possible values and conditions:
 * <table>
 * <tr>
 * <th>Sort portion</th>
 * <th>Possible values</th>
 * <th>Possible conditions</th>
 * </tr>
 * <tr>
 * <td>Sort By</td>
 * <td>Genre, Title, Date Added, Auto Rating, My Rating, Play Count : Total Overall, Play Count : Morning Totals, Play Count : Afternoon Totals, Play Count : Evening Totals, Play Count : Night Totals, Play Count : Total Weekday, Play Count : Total Weekend, Actor, Subtitle, Station name, Channel, Broadcast time, Director, Release Year, Writer, Producer, Date Recorded, Date Encoded, Bit Rate, Protection</td>
 * <td>Ascending, Descending, Random</td>
 * </tr>
 * </table>
 * When you use a fragment element to sort a playlist, you must sort on a metadata attribute that applies to the type of media items you are sorting.
 * For example, if you are sorting music items you can not sort on Actor.
 * The following table shows which metadata attributes you can use to sort which media types:
 * <table>
 * <tr>
 * <th>Media type</th>
 * <th>Possible metadata attributes</th>
 * </tr>
 * <tr>
 * <td>Music</td>
 * <td>Genre, Title, Date Added, Auto Rating, My Rating, Play Count : Total Overall, Play Count : Morning Totals, Play Count :Afternoon Totals, Play Count :Evening Totals, Play Count :Night Totals, Play Count :Total Weekday, Play Count : Total Weekend</td>
 * </tr>
 * <tr>
 * <td>Video or TV</td>
 * <td>Genre, Actor, Subtitle, Title, Date Added, Auto Rating, Station name, Channel, Broadcast time, Director, Release Year, Writer, Producer, Date Recorded, Date Encoded, Bit Rate, My Rating, Protection, Play Count : Total Overall, Play Count : Morning Totals, Play Count : Afternoon Totals, Play Count : Evening Totals, Play Count : Night Totals, Play Count : Total Weekday, Play Count : Total Weekend</td>
 * </tr>
 * <tr>
 * <td>Radio</td>
 * <td>Title, Date Added, Bit Rate</td>
 * </tr>
 * <tr>
 * <td>Photo</td>
 * <td>Title</td>
 * </tr>
 * <tr>
 * <td>Other</td>
 * <td>Genre, Title, Date Added, Auto Rating, My Rating, Bit Rate, Play Count : Total Overall, Play Count : Morning Totals, Play Count : Afternoon Totals, Play Count : Evening Totals, Play Count : Night Totals, Play Count : Total Weekday, Play Count : Total Weekend</td>
 * </tr>
 * </table>
 * Windows Media Player 9 Series or later.
 * @version $Revision: 92 $
 * @author Christophe Delory
 * @castor.class xml="fragment"
 */
public class Fragment
{
    /**
     * A list of argument elements.
     */
    private final List<Argument> _arguments = new ArrayList<Argument>();

    /**
     * A portion of a condition string.
     */
    private String _name = "";

    /**
     * Adds an argument element to this fragment.
     * @param argument an argument element. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>argument</code> is <code>null</code>.
     * @see #getArguments
     */
    public void addArgument(final Argument argument)
    {
        if (argument == null)
        {
            throw new NullPointerException("no argument");
        }

        _arguments.add(argument);
    }

    /**
     * Returns the list of argument elements.
     * @return a list of argument elements. May be empty but not <code>null</code>.
     * @see #addArgument
     * @castor.field
     *  get-method="getArguments"
     *  set-method="addArgument"
     *  type="christophedelory.playlist.wpl.Argument"
     *  collection="arraylist"
     * @castor.field-xml
     *  name="argument"
     *  node="element"
     */
    public List<Argument> getArguments()
    {
        return _arguments;
    }

    /**
     * Returns a portion of a condition string.
     * @return a fragment name. Shall not be <code>null</code>.
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
     * Initializes a portion of a condition string.
     * @param name a fragment name. Shall not be <code>null</code>.
     * @throws NullPointerException if <code>name</code> is <code>null</code>.
     * @see #getName
     */
    public void setName(final String name)
    {
        _name = name.trim(); // Throws NullPointerException if name is null.
    }
}
