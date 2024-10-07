//
// Created by dell on 2022/3/3.
//
#include <iostream>
using namespace std;

string Encode(const unsigned char *str, int btyes);

const string base64_table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

int main()
{
    unsigned char buff[10000];
    int index = 0;
    char temp;
    while ((temp = getchar()) != EOF)
    {
        buff[index] = temp;
        index++;
    }

    string result = Encode(buff, index);
    cout << result;

    return 0;
}

string Encode(const unsigned char *str, int btyes)
{
    string encode_result;
    const unsigned char *current = str;
    while (btyes > 2)
    {
        encode_result += base64_table[current[0] >> 2];
        encode_result += base64_table[((current[0] & 0x03) << 4) + (current[1] >> 4)];
        encode_result += base64_table[((current[1] & 0x0f) << 2) + (current[2] >> 6)];
        encode_result += base64_table[current[2] & 0x3f];

        current += 3;
        btyes -= 3;
    }

    if (btyes > 0)
    {
        encode_result += base64_table[current[0] >> 2];
        if (btyes % 3 == 1)
        {
            encode_result += base64_table[(current[0] & 0x03) << 4];
            encode_result += "==";
        }
        else if (btyes % 3 == 2)
        {
            encode_result += base64_table[((current[0] & 0x03) << 4) + (current[1] >> 4)];
            encode_result += base64_table[(current[1] & 0x0f) << 2];
            encode_result += "=";
        }
    }

    return encode_result;
}

