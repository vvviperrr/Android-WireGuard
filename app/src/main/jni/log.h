#ifndef WIREGUARD_LOG_H
#define WIREGUARD_LOG_H

#include <android/log.h>

#ifndef LOG_TAG
#error "LOG_TAG is not defined!"
#endif

#ifndef PRODUCTION_LOG
#define DPRINTF(level, fmt, args...) \
    do { \
        __android_log_print(level, LOG_TAG, fmt, ##args); \
    } while (0);
#else
#define DPRINTF(level, fmt, args...) do {} while (0);
#endif

#define LOGV(fmt, args...) DPRINTF(ANDROID_LOG_VERBOSE, fmt, ##args)
#define LOGD(fmt, args...) DPRINTF(ANDROID_LOG_DEBUG, fmt, ##args)
#define LOGI(fmt, args...) DPRINTF(ANDROID_LOG_INFO, fmt, ##args)
#define LOGW(fmt, args...) DPRINTF(ANDROID_LOG_WARN, fmt, ##args)
#define LOGE(fmt, args...) DPRINTF(ANDROID_LOG_ERROR, fmt, ##args)


#endif //WIREGUARD_LOG_H
