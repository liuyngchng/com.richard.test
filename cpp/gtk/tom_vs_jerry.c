/**
 * gcc -g  -o _gtk tom_vs_jerry.c `pkg-config --cflags --libs gtk+-2.0`
 */
#include <gtk/gtk.h>

/**
 * 每次移动 10 个像素
 */
#define _MV_OFFSET  	10

/**
 * 窗口宽度 单位：像素
 */
#define _WINDOW_WIDTH 	800

/**
 * 窗口高度 单位：像素
 */
#define _WINDOW_HEIGHT 	600

/**
 * 顶层容器，放置两个可固定位置的图标
 */
static GtkWidget *fixed;

/**
 * 角色1 button
 */
static GtkWidget *jerry;

/**
 * 角色2 button
 */
static GtkWidget *tom;

/**
 * 全局标志， jerry 是否需要被移动， 0:不移动, 1:移动
 */
static int is_jerry_to_move = 0;

/**
 * 全局标志， tom 是否需要被移动， 0:不移动, 1:移动
 */
static int is_tom_to_move = 0;


/* *
 * Create a new hbox with an image and a label packed into it
 * and return the box.
 * */
static GtkWidget *pic_label_box( gchar     *pic_filename,
                                 gchar     *label_text )
{
    GtkWidget *box;
    GtkWidget *label;
    GtkWidget *image;

    /* Create box for image and label */
    box = gtk_hbox_new (FALSE, 0);
    gtk_container_set_border_width (GTK_CONTAINER (box), 2);

    /* Now on to the image stuff */
    image = gtk_image_new_from_file (pic_filename);

    /* Create a label for the button */
    label = gtk_label_new (label_text);

    /* Pack the image and label into the box */
    gtk_box_pack_start (GTK_BOX (box), image, FALSE, FALSE, 3);
    gtk_widget_show (image);
    gtk_widget_show (label);

    return box;
}

void on_button_clicked(GtkButton *button, gpointer user_data) {
    g_print("I'm %s, can u see me?\n", (gchar *) user_data);
}

/**
 * 移动角色
 */
int mv_widget(GtkWidget * widget, int x_offset, int y_offset) {
	gint x = widget->allocation.x;
	gint y = widget->allocation.y;
	if((x + x_offset) < 0 ||  (x + x_offset) > _WINDOW_WIDTH) {
//		g_print("%s collide to left or right wall\n", gtk_label_get_text((GtkLabel *)widget));
		g_print("collide to left or right wall\n");
		return -1;
	}
	if((y + y_offset) < 0 ||  (y + y_offset) > _WINDOW_HEIGHT-120) {
		g_print("widget collide to up or down wall\n");
		return -1;
	}
	gtk_fixed_move (GTK_FIXED (fixed), widget, x + x_offset, y + y_offset);
	GList *list_child=gtk_container_get_children (GTK_CONTAINER (widget));
//	if(NULL == list_child) {
//		g_print("widget child is null\n");
//	} else {
//		GtkWidget *widget;
//		do {
//			widget =(GtkWidget *)list_child->data;
//			if(GTK_IS_LABEL(widget)) {
//				g_print("%s collide to left or right wall\n", gtk_label_get_text((GtkLabel *)widget));
//			}
////			gtk_label_get_type(widget);
//			list_child = list_child ->next;
//			if(NULL == list_child) {
//				break;
//			}
//			widget =(GtkWidget *)list_child->data;
//		} while ( widget != NULL);
//	}
//	gtk_widget_destroy((GtkWidget*)list_child);
	return 0;
}

// 上移
void mv_up(GtkWidget * widget) {
	mv_widget(widget, 0, -_MV_OFFSET);
}

// 下移
void mv_dn(GtkWidget * widget) {
	mv_widget(widget, 0, _MV_OFFSET);
}

// 左移
void mv_lft(GtkWidget * widget) {
	mv_widget(widget, -_MV_OFFSET, 0);
}

// 右移
void mv_rgt(GtkWidget * widget) {
	mv_widget(widget, _MV_OFFSET, 0);
}

gboolean mv_role_by_key(const char* role, guint key) {
	switch(key) {
	    case 'w':
	    case 'W':
	        g_print("%s move up\n", role);
	        mv_up(jerry);
	        break;
	    case 'a':
	    case 'A':
	    	g_print("%s move left\n", role);
	        mv_lft(jerry);
	        break;
	    case 'd':
	    case 'D':
	    	g_print("%s move right\n", role);
	        mv_rgt(jerry);
	        break;
	    case 's':
	    case 'S':
	    	g_print("%s move down\n", role);
	        mv_dn(jerry);
	        break;
	    case 'i':
	    case 'I':
	    case 65362:
	    	g_print("%s move up\n", role);
			mv_up(tom);
			break;
		case 'j':
		case 'J':
		case 65361:
			g_print("%s move left\n", role);
			mv_lft(tom);
			break;
		case 'l':
		case 'L':
		case 65363:
			g_print("%s move right\n", role);
			mv_rgt(tom);
			break;
		case 'k':
		case 'K':
		case 65364:
			g_print("%s move down\n", role);
			mv_dn(tom);
			break;
	    default:
	    	g_print("nothing done for key %d\n", key);
	    	break;
	    }
	      return FALSE;
}

/**
 * 按键按下时触发事件
 */
gboolean on_key_pressed(GtkWidget *widget,
		GdkEventKey *event, gpointer user_data) {
//	g_print("%s received key press %d\n", (gchar *) user_data, event->keyval);
	switch(event->keyval) {
	    case 'w':
	    case 'W':
	    case 'a':
	    case 'A':
	    case 'd':
	    case 'D':
	    case 's':
	    case 'S':
	    	if (is_jerry_to_move == 0) {
				g_print("Jerry start move\n");
				is_jerry_to_move = 1;
	    	}
	        mv_role_by_key("jerry", event->keyval);
	        break;
	    case 'i':
	    case 'I':
	    case 65362:
		case 'j':
		case 'J':
		case 65361:
		case 'l':
		case 'L':
		case 65363:
		case 'k':
		case 'K':
		case 65364:
			if (is_tom_to_move == 0) {
				g_print("Tom start move\n");
				is_tom_to_move = 1;
			}
			mv_role_by_key("tom", event->keyval);
			break;
	    default:
	    	g_print("nothing done for key_pressed %d\n", event->keyval);
	    	break;
	    }
	      return FALSE;
}



gboolean on_key_released(GtkWidget *widget,
		GdkEventKey *event, gpointer user_data) {

	switch(event->keyval) {
	    case 'w':
	    case 'W':
	    case 'a':
	    case 'A':
	    case 'd':
	    case 'D':
	    case 's':
	    case 'S':
	        g_print("Jerry stop move\n");
	        is_jerry_to_move = 0;
	        break;
	    case 'i':
	    case 'I':
	    case 65362:
		case 'j':
		case 'J':
		case 65361:
		case 'l':
		case 'L':
		case 65363:
		case 'k':
		case 'K':
		case 65364:
			g_print("Tom stop move\n");
			is_tom_to_move = 0;
			break;
	    default:
	    	g_print("nothing done for key_released %d\n", event->keyval);
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
    g_signal_connect(jerry, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Jerry");
    g_signal_connect(tom, "clicked", G_CALLBACK(on_button_clicked), (gpointer)"Tom");
    g_signal_connect(window, "key_press_event", G_CALLBACK(on_key_pressed), (gpointer)"test");
	g_signal_connect(window, "key_release_event", G_CALLBACK(on_key_released), (gpointer)"test");

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
