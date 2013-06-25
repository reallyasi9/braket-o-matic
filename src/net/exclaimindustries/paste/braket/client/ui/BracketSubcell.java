package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A BracketSubcell is half the height of its container.  It, in turn, can
 * contain up to two other widgets, either two more BracketSubcells or two
 * BracketCells. 
 * 
 * @author Nicholas Killewald
 */
public class BracketSubcell extends BracketDrawable {

    private static BracketSubcellUiBinder uiBinder = GWT
            .create(BracketSubcellUiBinder.class);

    interface BracketSubcellUiBinder extends UiBinder<Widget, BracketSubcell> {
    }

    // These are the spots in which we can stick our children.
    @UiField HTMLPanel container;
    @UiField DivElement topCell;
    @UiField DivElement bottomCell;
    
    // These are the actual components we're keeping track of.
    BracketDrawable mTop;
    BracketDrawable mBottom;
    
    public BracketSubcell() {
        initWidget(uiBinder.createAndBindUi(this));
        addStyleName("bracket-subcell");
        
    }
    
    public BracketSubcell(Direction dir) {
        this();
        setDirection(dir);
    }

    /**
     * Sets the children of this subcell to be more subcells.  The reason this
     * and the BracketCell version both exist is to ensure both children are of
     * the same type.
     * 
     * Note that you can only call this once.  Make a new BracketSubcell if you
     * need to replace the data.
     * 
     * @param top subcell on top
     * @param bottom subcell on bottom
     * @throws IllegalStateException this subcell was already initialized.
     */
    public void setChildren(BracketSubcell top, BracketSubcell bottom) throws IllegalStateException {
        setChildrenInternal(top, bottom);
    }

    /**
     * Sets the children of this subcell to be terminating cells.  The reason
     * this and the BracketSubcell version both exist is to ensure both children
     * are of the same type.
     * 
     * Note that you can only call this once.  Make a new BracketSubcell if you
     * need to replace the data.
     * 
     * @param top cell on top
     * @param bottom cell on bottom
     * @throws IllegalStateException this subcell was already initialized.
     */
    public void setChildren(BracketCell top, BracketCell bottom) throws IllegalStateException {
        setChildrenInternal(top, bottom);
    }
    
    private void setChildrenInternal(BracketDrawable top, BracketDrawable bottom) throws IllegalStateException {
        // If we've called this already, topCell and bottomCell should be
        // unparented, as we replace them in this call.
        if(topCell == null || bottomCell == null || !topCell.hasParentElement() || !bottomCell.hasParentElement())
            throw new IllegalStateException("This BracketSubcell was already initialized!  You can't init it twice!");

        container.addAndReplaceElement(top, topCell);
        container.addAndReplaceElement(bottom, bottomCell);

        top.setController(mController);
        bottom.setController(mController);
        
        mTop = top;
        mBottom = bottom;
    }
   
    @Override
    void fetchData() {
        mTop.fetchData();
        mBottom.fetchData();
    }

    @Override
    public void draw() {
        if(mTop == null || mBottom == null)
            throw new IllegalStateException("You didn't init the BracketSubcell yet!  What is WRONG with you?");
        
        mTop.setDirection(mDirection);
        mBottom.setDirection(mDirection);
        
        mTop.setPixelSize(getOffsetWidth(), getOffsetHeight() / 2);
        mBottom.setPixelSize(getOffsetWidth(), getOffsetHeight() / 2);
        
        mTop.draw();
        mBottom.draw();
    }
    
    @Override
    public void setController(BracketContainer controller) {
        super.setController(controller);
        if(mTop != null) mTop.setController(controller);
        if(mBottom != null) mBottom.setController(controller);
    }
}
