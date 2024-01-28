from django.shortcuts import render
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from .models import Number
from .serializers import NumberSerializer

# upload number information.


@api_view(['POST'])
def insert_number(request):
    serializer = NumberSerializer(data=request.data)

    if serializer.is_valid():
        serializer.save()
        return Response(serializer.data, status=status.HTTP_201_CREATED)
    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# get number information.
@api_view(['GET'])
def get_all_numbers(request):
    models = Number.objects.all()
    serializer = NumberSerializer(models, many=True)
    return Response(serializer.data)
