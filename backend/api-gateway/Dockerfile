FROM devopsfaith/krakend:2.7

COPY config/krakend.json /etc/krakend/krakend.json

EXPOSE 8090

CMD ["run", "-c", "/etc/krakend/krakend.json"]
