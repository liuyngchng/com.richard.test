#include "context.h"
#include "util.h"
#include "cJSON.h"
#include <stddef.h>

static struct context context;

static cJSON *mtx_data;

static const char *key1 = "key1";
static const char *key2 = "key2";
static const char *key3 = "key3";

void init(int **matrix, char *data)
{
    get_bin(matrix, 20, 10, context.board);
    mtx_data = cJSON_Parse(data);
    cJSON *grd_info = cJSON_GetObjectItem(mtx_data, key1);

}

cJSON *get_pce_info()
{

    cJSON *pce_info = cJSON_GetObjectItem(mtx_data, key2);
    cJSON *mat2d = cJSON_DetachItemFromObject(pce_info, key3);
//    cJSON_DeleteItemFromObject(pce_info, "mat2d");
    char *mat2d_str = cJSON_Print(mat2d);
    char *mat2d_int;
    int *array_int;
    get_int_array(mat2d, array_int, 4);
    cJSON *itemAdded = cJSON_Parse("{\"key\":\"value\"}");
    cJSON_AddItemToObjectCS(pce_info, key3, itemAdded);
    cJSON_Delete(mat2d);
    cJSON_Delete(j);

}

void get_int_array(char *str_a, int *a, int size)
{
    cJSON *json = cJSON_Parse(str_a);
    char *str1 = cJSON_Print(json);
    printf("string_json=%s\n", str1);
    int size1 = cJSON_GetArraySize(json);
    printf("sizeof str=%d\n", size1);

    cJSON *a0 = cJSON_GetArrayItem(json, 0);
    printf("a0_json=%s\n", cJSON_Print(a0));
    int size2 = cJSON_GetArraySize(a0);
    printf("sizeof a0=%d\n", size2);
    for (int i = 0; i < cJSON_GetArraySize(json); i++) {
        int sum = 0;
//        int size = cJSON_GetArraySize(cJSON_GetArrayItem(json, 0));
        for (int j = 0; j < size; j++) {
            sum += (int)cJSON_GetNumberValue(
                cJSON_GetArrayItem(cJSON_GetArrayItem(json, i), j)
            ) * pow(2, j);
        }
        a[i] = sum;
    }
    cJSON_Delete(json);
}
