/**
 * This file is part of braket-o-matic.
 *
 * braket-o-matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * braket-o-matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.exclaimindustries.paste.braket.client.resources;

import org.vectomatic.dom.svg.ui.ExternalSVGResource;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author paste
 * 
 */
public interface Resources extends ClientBundle {

    public static final Resources INSTANCE = GWT.create(Resources.class);

    public interface Style extends CssResource {
        String blueButton();

        String redButton();

        String dialogBox();

        String glass();

        String dialogText();

        String buttonContainer();

        String formDescription();

        String leftButton();

        String rightButton();

        String formField();

        String formContainer();
        
        String opaque();
        
        String transparent();

    }

    @Source("style.css")
    Style style();

    @Source("g+-icon.svg")
    ImageResource gPlusIcon();

    @Source("backColors.gif")
    ImageResource backColors();

    @Source("bold.gif")
    ImageResource bold();

    @Source("createLink.gif")
    ImageResource createLink();

    @Source("fonts.gif")
    ImageResource fonts();

    @Source("fontSizes.gif")
    ImageResource fontSizes();

    @Source("foreColors.gif")
    ImageResource foreColors();

    @Source("hr.gif")
    ImageResource hr();

    @Source("indent.gif")
    ImageResource indent();

    @Source("insertImage.gif")
    ImageResource insertImage();

    @Source("italic.gif")
    ImageResource italic();

    @Source("justifyCenter.gif")
    ImageResource justifyCenter();

    @Source("justifyLeft.gif")
    ImageResource justifyLeft();

    @Source("justifyRight.gif")
    ImageResource justifyRight();

    @Source("ol.gif")
    ImageResource ol();

    @Source("outdent.gif")
    ImageResource outdent();

    @Source("removeFormat.gif")
    ImageResource removeFormat();

    @Source("removeLink.gif")
    ImageResource removeLink();

    @Source("strikeThrough.gif")
    ImageResource strikeThrough();

    @Source("subscript.gif")
    ImageResource subscript();

    @Source("superscript.gif")
    ImageResource superscript();

    @Source("ul.gif")
    ImageResource ul();

    @Source("underline.gif")
    ImageResource underline();

    @Source("HelpImage.jpg")
    ImageResource help();

    @Source("header.svg")
    ExternalSVGResource headerSVG();

}
