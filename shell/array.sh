a=(hello who "are you" "where are you?")

for (( i=0;i<${#a[@]};i++  )) do
echo 'a['$i']='${a[i]};
done;
b=(`ls`)
