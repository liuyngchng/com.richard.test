/**
 *  参考 gtk_process_bar.c
 */
#include <gtk/gtk.h>

static void print_hello(GtkWidget *widget, gpointer data){
	g_print("button clicked\n");
}

static void callback( GtkWidget *widget,gpointer   data ){
    g_print("Hello again - %s was pressed\n", (gchar *) data);
}

static gboolean delete_event( GtkWidget *widget,GdkEvent  *event, gpointer   data ){
    gtk_main_quit ();
    return FALSE;
}

static void destroy( GtkWidget *widget, gpointer data){
    gtk_main_quit ();
}
int main(int argc, char*argv[]) {
    GtkWidget *window;
    GtkWidget *button;
    GtkWidget *box1;

    gtk_init(&argc, &argv);
    /* create the main, top level, window */
    window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    g_signal_connect(window, "destroy", G_CALLBACK(destroy), NULL);
    g_signal_connect(window, "delete-event", G_CALLBACK(delete_event), NULL);

    gtk_window_set_title(GTK_WINDOW(window),"Hello windows");
//    gtk_window_set_default_size(GTK_WINDOW(window), 800, 600);
    gtk_container_set_border_width(GTK_CONTAINER(window), 5);
    box1 = gtk_hbox_new(FALSE, 0);
    gtk_container_add(GTK_CONTAINER (window), box1);
    button = gtk_button_new_with_label("button1");
    // 设置按钮的大小为200x50像素
    gtk_widget_set_size_request(button, 200, 50);
//    GtkWidget* gtk_alignment_new( gfloat xalign,
//                                  gfloat yalign,
//                                  gfloat xscale,
//                                  gfloat yscale );
//
//    void gtk_alignment_set( GtkAlignment *alignment,
//                            gfloat        xalign,
//                            gfloat        yalign,
//                            gfloat        xscale,
//                            gfloat        yscale );
    GtkAlignment align = gtk_alignment_new(0.5, 0.5, 0.5, 0.5);

    // 构造按钮触发事件
    g_signal_connect (button, "clicked", G_CALLBACK(callback), (gpointer)"button 1");
    //点击后会导致父窗体推出
//    g_signal_connect_swapped(button, "clicked", G_CALLBACK(gtk_widget_destroy), window);
//    gtk_container_add(GTK_CONTAINER(window), button);
    gtk_box_pack_start(GTK_BOX(box1), button, TRUE, TRUE, 0);


    // 全部显示出来，包括其中的子元素
    gtk_widget_show_all(window);

//    gtk_widget_show(button);
//    gtk_widget_show(window);
    //一直处于休眠状态，直到有事件到达，开始响应事件
    gtk_main();
    return 0;
}
