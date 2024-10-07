import pandas as pd


def getCitationTable(database):
    df = pd.read_csv('data/{}.csv'.format(database))
    cite = df["ACM Ref"].tolist()
    type = df["类别"].tolist()
    filtered_cite = []
    for t,c in zip(type,cite):
        if t == "Controlled Experiments":
            filtered_cite.append(c)
    id = [x+1 for x in range(len(filtered_cite))]
    df_res = pd.DataFrame()
    df_res["ID"] = id
    df_res["CITATION"] = filtered_cite
    markdown_table = df_res.to_markdown(index=False)
    print(markdown_table)


if __name__ == '__main__':
    print("EASE")
    getCitationTable("EASE")
    print("-" * 20)
    print("EMSE")
    getCitationTable("EMSE")
    print("-" * 20)
    print("ESEM")
    getCitationTable("ESEM")
