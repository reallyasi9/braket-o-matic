package net.exclaimindustries.paste.braket.client.ui;

public abstract class PendingChange<TargetType> {

  private final TargetType target;

  public PendingChange(TargetType target) {
    this.target = target;
  }

  /**
   * Commit the change to the object.
   */
  public void commit() {
    doCommit(target);
  }

  /**
   * Update the appropriate field.
   * 
   * @param objecy
   *          the object to update
   * @param value
   *          the new value
   */
  protected abstract void doCommit(TargetType target);

}
