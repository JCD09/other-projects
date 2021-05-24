#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <string.h>

#include "Definitions.h"

void write_to_file(string file_path, string data){
    FILE *file = fopen(file_path, "w");

    if (file == NULL) {
      printf("ERROR OPENING %s.", file_path);
      exit(1);
    }

    int charWritten = fprintf(file,"%s", data);

    if (charWritten <= 0) {
      printf("ERROR WRITING DATA");
      exit(1);
    }

    fclose(file);
}
