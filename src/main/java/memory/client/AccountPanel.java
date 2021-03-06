//
// $Id$

package memory.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.threerings.gwt.ui.Popups;
import com.threerings.gwt.ui.Widgets;

import memory.data.AccessInfo;
import memory.rpc.DataService;
import memory.rpc.DataServiceAsync;

/**
 * Displays account information and allows for cortex management.
 */
public class AccountPanel extends Composite
{
    public AccountPanel (final DataService.AccountResult data)
    {
        initWidget(_binder.createAndBindUi(this));

        _nickname.setText(data.nickname);
        _nickname.setTitle(data.userId);
        _logout.setHref(data.logoutURL);

        _helper = new CortexNameHelper(_name, _urltip, _create);

        addOwnedLinks(data.owned);
        addSharedLinks(data.shared);

        new MClickCallback<Void>(_create, _name) {
            protected boolean callService () {
                if (!_helper.processName()) {
                    return false;
                }
                _nname = _name.getText().trim();
                _datasvc.createCortex(_nname, this);
                return true;
            }
            protected boolean gotResult (Void result) {
                data.owned.add(_nname);
                addOwnedLinks(data.owned);
                Popups.infoBelow(
                    "Cortex created! Click the link above to start using it.", getPopupNear());
                _name.setText("");
                _helper.processName();
                return false;
            }
            protected String _nname;
        };
    }

    protected void addOwnedLinks (List<String> cortexen)
    {
        _owned.clear();
        if (cortexen != null) {
            for (final String cortex : cortexen) {
                FlowPanel bits = new FlowPanel();
                bits.add(DestroyPopup.createIcon(cortex, bits));
                bits.add(AccessPopup.createAccessIcon(new ClickHandler() {
                    public void onClick (ClickEvent event) {
                        CortexAccessPopup.show(cortex, (Widget)event.getSource());
                    }
                }));
                bits.add(new Anchor(cortex, "/c/" + cortex.toLowerCase()));
                _owned.add(bits);
            }
        }
        if (_owned.getWidgetCount() == 0) {
            _owned.add(Widgets.newLabel("(none)", _styles.none()));
        }
    }

    protected void addSharedLinks (List<AccessInfo> cortexen)
    {
        _shared.clear();
        if (cortexen != null) {
            for (AccessInfo info : cortexen) {
                _shared.add(new Anchor(info.cortexId, "/c/" + info.cortexId.toLowerCase()));
            }
        }
        if (_shared.getWidgetCount() == 0) {
            _shared.add(Widgets.newLabel("(none)", _styles.none()));
        }
    }

    protected interface Styles extends CssResource
    {
        String none ();
    }
    protected @UiField Styles _styles;
    protected @UiField MemoryResources _rsrc;

    protected @UiField Label _nickname;
    protected @UiField Anchor _logout;
    protected @UiField FlowPanel _owned;
    protected @UiField FlowPanel _shared;
    protected @UiField HTML _urltip;

    protected @UiField TextBox _name;
    protected @UiField Button _create;

    protected final CortexNameHelper _helper;

    protected interface Binder extends UiBinder<Widget, AccountPanel> {}
    protected static final Binder _binder = GWT.create(Binder.class);
    protected static final DataServiceAsync _datasvc = GWT.create(DataService.class);
}
