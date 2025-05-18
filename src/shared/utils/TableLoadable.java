
package shared.utils;

import javax.swing.table.DefaultTableModel;

public interface TableLoadable {
    Object[] toTableRow();
    String[] getColumnNames();
}
