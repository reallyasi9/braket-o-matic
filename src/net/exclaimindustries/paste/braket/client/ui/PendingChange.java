package net.exclaimindustries.paste.braket.client.ui;

public abstract class PendingChange<TargetType, ValueType> {

  private final TargetType target;
  private final ValueType value;

  public PendingChange(TargetType target, ValueType value) {
    this.target = target;
    this.value = value;
  }

  /**
   * Commit the change to the object.
   */
  public void commit() {
    doCommit(target, value);
  }

  /**
   * Update the appropriate field.
   * 
   * @param objecy
   *          the object to update
   * @param value
   *          the new value
   */
  protected abstract void doCommit(TargetType target, ValueType value);

}
