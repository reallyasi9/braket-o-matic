package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A BracketColumn can hold one BracketDrawable, generally either a Subcell or
 * a Cell.  This represents one distinct round in the tournament.
 * 
 * @author Nicholas Killewald
 */
public class BracketColumn extends BracketDrawable {

    private static BracketColumnUiBinder uiBinder = GWT
            .create(BracketColumnUiBinder.class);

    interface BracketColumnUiBinder extends UiBinder<Widget, BracketColumn> {
    }

    @UiField HTMLPanel container;
    @UiField DivElement theDiv;
    
    BracketDrawable mChild;

    public BracketColumn() {
        initWidget(uiBinder.createAndBindUi(this));
        addStyleName("bracket-column");
    }
    
    public BracketColumn(Direction dir) {
        this();
        setDirection(dir);
    }
    
    public void setChild(BracketDrawable child) throws IllegalStateException {
        if(theDiv == null || !theDiv.hasParentElement())
            throw new IllegalStateException("This BracketColumn was already initialized!");
        
        container.addAndReplaceElement(child, theDiv);
        mChild = child;
        mChild.setController(mController);
    }

    @Override
    void fetchData() {
        mChild.fetchData();
    }

    @Override
    public void draw() {
        if(mChild == null)
            throw new IllegalStateException("You didn't init this BracketColumn yet!  Geez!");

        mChild.setDirection(mDirection);
        mChild.setPixelSize(getOffsetWidth(), getOffsetHeight());
        mChild.fetchData();
        mChild.draw();
    }
    
    @Override
    public void setController(BracketContainer controller) {
        super.setController(controller);
        if(mChild != null) mChild.setController(controller);
    }
}
