package net.exclaimindustries.paste.braket.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BraketAdminLayout extends Composite {

  interface BraketAdminLayoutUiBinder extends
      UiBinder<Widget, BraketAdminLayout> {
  }

  private static BraketAdminLayoutUiBinder uiBinder = GWT
      .create(BraketAdminLayoutUiBinder.class);

  public BraketAdminLayout() {
    initWidget(uiBinder.createAndBindUi(this));
  }

}
