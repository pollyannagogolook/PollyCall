from rest_framework import serializers
from .models import Number

# Serializers define the API representation.


class NumberSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Number
        fields = ["number", "owner", "is_scam"]
