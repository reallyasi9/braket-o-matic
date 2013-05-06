/**
 * BracketDrawable.java
 * Copyright (C)2013 Nicholas Killewald
 * 
 * This file is distributed under the terms of the BSD license.
 * The source package should have a LICENCE file at the toplevel.
 */
package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.user.client.ui.Composite;

/**
 * BracketDrawable is the base class for any of the drawable things (besides
 * BracketContainer).  This sort of thing can be given a direction and told to
 * go draw in that direction.
 * 
 * @author Nicholas Killewald
 */
public abstract class BracketDrawable extends Composite {
    /**
     * Draws whatever this is.  It may recurse down the chain.
     */
    public abstract void draw();

    public enum Direction {
        /** A bracket on the left. */
        LEFT,
        /** A bracket on the right. */
        RIGHT,
        /** The final bout (really only valid as a direct BracketCell). */
        FINAL
    };
    
    protected Direction mDirection = Direction.LEFT;

    /** This is from whence we get data, and lots of it. */
    protected BracketContainer mController;

    /**
     * Assigns the BracketContainer to be used as a controller for this item.
     * This is generally assigned when BracketContainer makes all its Drawables.
     *
     * @param controller the BracketContainer that starts it all off
     */
    void setController(BracketContainer controller) {
        mController = controller;
    }

    /**
     * Tells this Drawable to fetch (or refetch) its data from the controller.
     * In general, this is called on BracketColumns and iterates down the chain.
     * Make sure you call this AFTER the controller is assigned!
     * @return 
     */
    abstract void fetchData();
    
    /**
     * Assigns a direction to this drawable.  In general, this is assigned to a 
     * top-level BracketColumn, which then assigns it to each of its Subcells
     * as it draws them, which in turn assigns it to each of ITS Subcells and
     * Cells until it bottoms out.
     * 
     * @param dir Direction of this drawable thingamajig
     */
    public void setDirection(Direction dir) { mDirection = dir; }
}
