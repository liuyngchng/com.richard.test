#include <stdio.h>
#include <string.h>

typedef struct student {
    char class;
    int grade;
    long array[3];
    int *point;
}student_t;

typedef struct nest_stu {
    char rank;
    student_t nest_stu;
    student_t strct_array[2];
    student_t *strct_point;
    student_t *strct_point_array[2];
}nest_stu_t;

typedef struct g_student {
    int g_grade;
}g_stu;

g_stu g_stu_t = {11};

int test_func(char char_arg, int int_arg, float float_arg, char *stu_buf, char *nest_stu_buf, char *out_buf)
{
    //data type test
    printf("char arg: %c\n", char_arg);
    printf("int arg: %d\n", int_arg);
    printf("float arg: %f\n", float_arg);

    student_t *stu_p = NULL;
    nest_stu_t *nest_stu_p = NULL;

    stu_p = (student_t *)stu_buf;
    nest_stu_p = (nest_stu_t *)nest_stu_buf;
    //struct type test
    printf("struct class: %c\n", stu_p->class);
    printf("struct grade: %d\n", stu_p->grade);
    printf("struct array[0]: %d array[1]: %d\n", stu_p->array[0], stu_p->array[1]);
    printf("struct point: %d\n", *(stu_p->point));

    //nest struct test
    printf("nest struct rank: %d\n", nest_stu_p->rank);
    printf("nest struct stu grade: %d\n", nest_stu_p->nest_stu.grade);

    //struct array
    printf("nest struct array[0] grade: %d\n", nest_stu_p->strct_array[0].grade);
    printf("nest struct array[1] grade: %d\n", nest_stu_p->strct_array[1].grade);

    //struct point
    printf("nest struct point grade: %d\n", nest_stu_p->strct_point->grade);
    //struct point array
    printf("nest struct point array[0] grade: %d\n", nest_stu_p->strct_point_array[0]->grade);
    printf("nest struct point array[1] grade: %d\n", nest_stu_p->strct_point_array[1]->grade);

    //out buf test
    memcpy(out_buf, stu_p, sizeof(int)*2);

    return 1;
}