#include <gtk/gtk.h>

/**
 * 每次移动 10 个像素
 */
#define _MV_OFFSET  	10

#define _WINDOW_WIDTH 	800

#define _WINDOW_HEIGHT 	600

/* *
 * Create a new hbox with an image and a label packed into it
 * and return the box.
 * */
static GtkWidget *pic_label_box( gchar     *xpm_filename,
                                 gchar     *label_text )
{
    GtkWidget *box;
    GtkWidget *label;
    GtkWidget *image;

    /* Create box for image and label */
    box = gtk_hbox_new (FALSE, 0);
    gtk_container_set_border_width (GTK_CONTAINER (box), 2);

    /* Now on to the image stuff */
    image = gtk_image_new_from_file (xpm_filename);

    /* Create a label for the button */
    label = gtk_label_new (label_text);

    /* Pack the image and label into the box */
    gtk_box_pack_start (GTK_BOX (box), image, FALSE, FALSE, 3);
    gtk_box_pack_start (GTK_BOX (box), label, FALSE, FALSE, 3);
    gtk_widget_show (image);
    gtk_widget_show (label);

    return box;
}

static GtkWidget *fixed;

static GtkWidget *jerry;
static GtkWidget *tom;

void on_button_clicked(GtkButton *button, gpointer user_data) {
    g_print("I'm %s, can u see me?\n", (gchar *) user_data);
}

int mv_widget(GtkWidget * widget, int x_offset, int y_offset) {
	gint x = widget->allocation.x;
	gint y = widget->allocation.y;
	if((x + x_offset) < 0 ||  (x + x_offset) > _WINDOW_WIDTH) {
		g_print("widget collide to left or right wall\n");
		return -1;
	}
	if((y + y_offset) < 0 ||  (y + y_offset) > _WINDOW_HEIGHT-80) {
		g_print("widget collide to up or down wall\n");
		return -1;
	}
	gtk_fixed_move (GTK_FIXED (fixed), widget, x + x_offset, y+ y_offset);
	return 0;
}

void mv_up(GtkWidget * widget) {
	mv_widget(widget, 0, -_MV_OFFSET);
}

void mv_dn(GtkWidget * widget) {
	mv_widget(widget, 0, _MV_OFFSET);
}

void mv_lft(GtkWidget * widget) {
	mv_widget(widget, -_MV_OFFSET, 0);
}

void mv_rgt(GtkWidget * widget) {
	mv_widget(widget, _MV_OFFSET, 0);
}

gboolean on_key_pressed(GtkWidget *widget,
		GdkEventKey *event, gpointer user_data) {
//	g_print("%s received key press %d\n", (gchar *) user_data, event->keyval);
	switch(event->keyval) {
	    case 'w':
	    case 'W':
	        g_print("Move Jerry Up\n");
	        mv_up(jerry);
	        break;
	    case 'a':
	    case 'A':
	        g_print("Move Jerry Left\n");
	        mv_lft(jerry);
	        break;
	    case 'd':
	    case 'D':
	        g_print("Move Jerry Right\n");
	        mv_rgt(jerry);
	        break;
	    case 's':
	    case 'S':
	        g_print("Move Jerry Down\n");
	        mv_dn(jerry);
	        break;
	    case 'i':
	    case 'I':
	    case 65362:
			g_print("Move Tom Up\n");
			mv_up(tom);
			break;
		case 'j':
		case 'J':
		case 65361:
			g_print("Move Tom Left\n");
			mv_lft(tom);
			break;
		case 'l':
		case 'L':
		case 65363:
			g_print("Move Tom Right\n");
			mv_rgt(tom);
			break;
		case 'k':
		case 'K':
		case 65364:
			g_print("Move Tom Down\n");
			mv_dn(tom);
			break;
	    default:
	    	g_print("nothing done for key %d\n", event->keyval);
	    	break;
	    }
	      return FALSE;
}

int main(int argc, char *argv[]) {
    gtk_init(&argc, &argv);

    GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_default_size(GTK_WINDOW(window), _WINDOW_WIDTH, _WINDOW_HEIGHT);
    gtk_window_set_title(GTK_WINDOW(window),"Tom and Jerry Game");
    fixed = gtk_fixed_new();
    jerry = gtk_button_new();

    tom = gtk_button_new();

    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    g_signal_connect(jerry, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Tom");
    g_signal_connect(tom, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Jerry");
    g_signal_connect(window, "key_press_event", G_CALLBACK(on_key_pressed), (gpointer)"test");
    GtkWidget *box1 = pic_label_box ("jerry.png", "Jerry");
    gtk_container_add (GTK_CONTAINER (jerry), box1);
    GtkWidget *box2 = pic_label_box ("tom.png", "Tom");
        gtk_container_add (GTK_CONTAINER (tom), box2);
    gtk_container_add(GTK_CONTAINER(window), fixed);
    gtk_fixed_put(GTK_FIXED(fixed), jerry, 0, 300);
    gtk_fixed_put(GTK_FIXED(fixed), tom, 800, 300);
    // test move
//    gtk_fixed_move (GTK_FIXED (fixed), button1, 10, 20);
    gtk_widget_show_all(window);
    gtk_main();

    return 0;
}
